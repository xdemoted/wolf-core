package com.wolfco.main.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.Utilities;
import com.wolfco.common.classes.Argument;
import com.wolfco.common.classes.ArgumentType;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CommandTypes;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.main.Core;
import com.wolfco.main.classes.Home;
import com.wolfco.main.classes.PlayerData;
import com.wolfco.main.handlers.PermissionHandler;

import net.kyori.adventure.audience.Audience;
import net.luckperms.api.model.user.User;

public class SetHome implements CoreCommandExecutor {
    static final String NODE = "wolfcore.sethome";

    @Override
    public Command getCommand() {
        Command command = new Command("sethome");
        command.setDescription("Set a home location");
        command.setNode(NODE);
        command.setAccessType(CommandTypes.PLAYER);
        command.addArgument(new Argument(ArgumentType.ALPHANUMERICSTRING, false).setName("HOME"));

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

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        String home = (String) argumentValues[0];
        Audience audience = fetchCore().getAdventure().sender(sender);
        User user = fetchCore().getLuckPerms().getUserManager().getUser(sender.getName());

        if (home == null) {
            home = "home";
        }

        PlayerData playerData = core.getPlayerManager().getPlayerData((Player) sender);

        if (playerData != null) {
            if (playerData.homes.size() == PermissionHandler.getNumberValue(NODE, user).intValue()) {
                Utilities.sendColorText(audience, core.getMessage("home.limit", List.of(PermissionHandler.getNumberValue(NODE, user).toString())));
                return true;
            }

            playerData.homes.put(home, new Home(home, ((Player) sender).getLocation()));
            
            Utilities.sendColorText(audience, core.getMessage("home.set", List.of(home)));
        } else {
            Utilities.sendColorText(audience, core.getMessage("generic.invaliddata"));
        }
        return true;
    }

}
