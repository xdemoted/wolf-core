package com.wolfco.main.commands;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.main.Core;
import com.wolfco.main.classes.customargs.HomeArgument;
import com.wolfco.main.classes.mongoDB.subtypes.NamedLocation;

public class Home implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("home");
        command.setAccessType(AccessType.PLAYER);
        command.addArguments(new HomeArgument(false));

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public Home(Core core) {
        this.core = core;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        var homeFuture = (CompletableFuture<NamedLocation>) argumentValues[0];
        var dataFuture = core.getPlayerManager().getPlayerData((Player) sender);

        dataFuture.thenAccept(data -> {
            if (data == null) {
                core.sendPreset(sender, "generic.invaliddata");
                return;
            }

            homeFuture.thenAccept(home -> {
                if (home == null) {
                    core.sendPreset(sender, "home.notfound", List.of(args[0]));
                    return;
                }

                home.teleport((Player) sender);
                core.sendPreset(sender, "home.teleported", List.of(home.getName()));
            });
        });

        return true;
    }
}
