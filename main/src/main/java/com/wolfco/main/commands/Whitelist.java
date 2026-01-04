package com.wolfco.main.commands;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.common.commands.arguments.OfflinePlayerArg;
import com.wolfco.common.commands.arguments.SubCommandArg;
import com.wolfco.main.Core;
import com.wolfco.main.whitelist.WhitelistManager;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class Whitelist implements CoreCommand {
    private final Core core;
    private final WhitelistManager whitelistManager;

    @Inject
    public Whitelist(Core core, WhitelistManager whitelistManager) {
        this.core = core;
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
    
    @Override
    public boolean onCommand(CommandSourceStack commandStack, String[] args, Object[] argumentValues) {
        Command command = (Command) argumentValues[0];

        switch (command.getName()) {
            case "add" -> {
                CompletableFuture<UUID> playerFuture = (CompletableFuture<UUID>) command.getValues(commandStack, Arrays.copyOfRange(args, 1, 1))[0];
                return handleAdd(commandStack, playerFuture);
            }
            case "remove" -> {
                CompletableFuture<UUID> playerFuture = ((CompletableFuture<UUID>) argumentValues[1]);
                return handleRemove(commandStack, playerFuture);
            }
            case "list" -> {
                return handleList(commandStack);
            }
        }

        return false;
    }

    private boolean handleAdd(CommandSourceStack commandStack, CompletableFuture<UUID> playerFuture) {
        playerFuture.thenAcceptAsync(playerUUID -> {
            int result = whitelistManager.addToWhitelist(playerUUID);

            switch (result) {
                case 0 -> core.sendPreset(commandStack.getSender(), "whitelist.add.success");
                case 1 -> core.sendPreset(commandStack.getSender(), "whitelist.add.already");
                default -> core.sendPreset(commandStack.getSender(), "whitelist.add.failure");
            }
        });
        return false;
    }

    private boolean handleRemove(CommandSourceStack commandStack, CompletableFuture<UUID> playerFuture) {
        playerFuture.thenAcceptAsync(uuid -> {
            int result = whitelistManager.removeFromWhitelist(uuid);

            switch (result) {
                case 0 -> core.sendPreset(commandStack.getSender(), "whitelist.remove.success");
                case 1 -> core.sendPreset(commandStack.getSender(), "whitelist.remove.notwhitelisted");
                default -> core.sendPreset(commandStack.getSender(), "whitelist.remove.failure");
            }
        });
        return false;
    }

    private boolean handleList(CommandSourceStack commandStack) {
        return false;
    }
}
