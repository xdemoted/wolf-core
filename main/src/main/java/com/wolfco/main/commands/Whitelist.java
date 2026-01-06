package com.wolfco.main.commands;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.common.commands.arguments.OfflinePlayerArg;
import com.wolfco.common.commands.arguments.SubCommandArg;
import com.wolfco.main.whitelist.WhitelistManager;
import com.wolfco.common.MessageUtility;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class Whitelist implements CoreCommand {
    private final WhitelistManager whitelistManager;

    @Inject
    public Whitelist(WhitelistManager whitelistManager) {
        this.whitelistManager = whitelistManager;
    }

    @Override
    public Command getCommand() {
        Command command = new Command().setName("whitelist");
        command.setAccessType(AccessType.PLAYER);

        Command add = new Command().setName("add").addArguments(
            new OfflinePlayerArg(true)
        );

        Command remove = new Command().setName("remove").addArguments(
            new OfflinePlayerArg(true)
        );

        Command list = new Command().setName("list");

        command.addArguments(
            new SubCommandArg(true).addCommands(add,remove,list)
        );

        return command;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean onCommand(CommandSourceStack commandStack, String[] args, Object[] argumentValues) {
        Command command = (Command) argumentValues[0];

        switch (command.getName()) {
            case "add" -> {
                CompletableFuture<UUID> playerFuture = (CompletableFuture<UUID>) command.getValues(commandStack, Arrays.copyOfRange(args, 1, 1))[0];
                return handleAdd(commandStack, playerFuture, args);
            }
            case "remove" -> {
                CompletableFuture<UUID> playerFuture = ((CompletableFuture<UUID>) argumentValues[1]);
                return handleRemove(commandStack, playerFuture, args);
            }
            case "list" -> {
                StringBuilder sb = new StringBuilder();
                sb.append("Whitelisted players: ");
                sb.append(String.join(", ", whitelistManager.getWhitelistedNames()));
                commandStack.getSender().sendMessage(sb.toString());
                return true;
            }
        }

        return false;
    }

    private boolean handleAdd(CommandSourceStack commandStack, CompletableFuture<UUID> playerFuture, String[] args) {
        playerFuture.thenAcceptAsync(playerUUID -> {
            int result = whitelistManager.addToWhitelist(args[1], playerUUID);

            switch (result) {
                case 0 -> MessageUtility.sendPreset(commandStack.getSender(), "whitelist.add.success");
                case 1 -> MessageUtility.sendPreset(commandStack.getSender(), "whitelist.add.already");
                default -> MessageUtility.sendPreset(commandStack.getSender(), "whitelist.add.failure");
            }
        });
        return false;
    }

    private boolean handleRemove(CommandSourceStack commandStack, CompletableFuture<UUID> playerFuture, String[] args) {
        playerFuture.thenAcceptAsync(uuid -> {
            int result = whitelistManager.removeFromWhitelist(args[1], uuid);

            switch (result) {
                case 0 -> MessageUtility.sendPreset(commandStack.getSender(), "whitelist.remove.success");
                case 1 -> MessageUtility.sendPreset(commandStack.getSender(), "whitelist.remove.notwhitelisted");
                default -> MessageUtility.sendPreset(commandStack.getSender(), "whitelist.remove.failure");
            }
        });
        return false;
    }
}
