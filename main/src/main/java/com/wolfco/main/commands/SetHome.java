package com.wolfco.main.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.common.commands.arguments.StringArg;
import com.wolfco.main.Core;
import com.wolfco.main.classes.Home;
import com.wolfco.main.classes.PlayerData;
import com.wolfco.main.handlers.PermissionHandler;

import net.luckperms.api.model.user.User;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class SetHome implements CoreCommand {    static final String NODE = "wolfcore.sethome";

    @Override
    public Command getCommand() {
        Command command = new Command().setName("sethome");
        command.setAccessType(AccessType.PLAYER);
        command.addArguments(new StringArg(false, true, false).setName("HOME"));

        return command;
    }

    @Inject
    Core core;

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        String home = (String) argumentValues[0];
        User user = core.getLuckPerms().getUserManager().getUser(sender.getName());

        if (home == null) {
            home = "home";
        }

        PlayerData playerData = core.getPlayerManager().getPlayerData((Player) sender);

        if (playerData != null) {
            if (playerData.homes.size() >= PermissionHandler.getNumberValue(NODE, user).intValue() && !playerData.homes.containsKey(home)) {
                core.sendPreset(sender, "home.limit", List.of(PermissionHandler.getNumberValue(NODE, user).toString()));
                return true;
            }

            playerData.homes.put(home, new Home(home, ((Player) sender).getLocation()));
            
            core.sendPreset(sender, "home.set", List.of(home));
        } else {
            core.sendPreset(sender, "generic.invaliddata");
        }
        return true;
    }

}
