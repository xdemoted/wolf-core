package com.wolfco.main.commands;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.argumenthandlers.StringArg;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.main.Core;
import com.wolfco.main.classes.mongoDB.subtypes.NamedLocation;

import net.luckperms.api.model.user.User;

public class SetHome implements CoreCommandExecutor {
    static final String NODE = "wolfcore.sethome";

    @Override
    public Command getCommand() {
        Command command = new Command("sethome");
        command.setAccessType(AccessType.PLAYER);
        command.addArguments(new StringArg(false, true, false).setName("HOME"));

        return command;
    }

    Core core;

    public SetHome(Core core) {
        this.core = core;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    // TODO: Finish Sethome Command

    @SuppressWarnings("unchecked")
    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args,
            Object[] argumentValues) {
        var homeFuture = (CompletableFuture<NamedLocation>) argumentValues[0];
        var dataFuture = core.getPlayerManager().getPlayerData((Player) sender);
        User user = fetchCore().getLuckPerms().getUserManager().getUser(sender.getName());

        dataFuture.thenAccept(data -> {
            if (data == null) {
                core.sendPreset(sender, "generic.invaliddata");
                return;
            }

            homeFuture.thenAccept(home -> {
                if (home == null) {
                    core.sendPreset(sender, "home.notfound", List.of(args[0]));
                }
            });
        });
        return true;
    }

}
