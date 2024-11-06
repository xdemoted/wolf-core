package com.wolfco.main.commands;

import com.wolfco.main.core;
import com.wolfco.main.classes.Home;
import com.wolfco.main.classes.PlayerData;
import com.wolfco.common.utils;
import com.wolfco.common.classes.Argument;
import com.wolfco.common.classes.ArgumentType;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.main.utilities.permissionHandler;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.kyori.adventure.audience.Audience;
import net.luckperms.api.model.user.User;

import java.util.ArrayList;

public class sethome implements CoreCommandExecutor {
    @Override
    public Command getCommand() {
        return new Command("sethome","wolfcore.sethome", new ArrayList<>() {{
            add(new Argument("home", ArgumentType.STRING, true));
        }});
    }

    @Override
    public core fetchCore() {
        return core;
    }
    
    core core;
    public sethome(core core) {
        this.core = core;
    }
    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        Audience audience = core.adventure().sender(sender);
        User user = core.lp.getUserManager().getUser(sender.getName());
        if (!(sender instanceof Player)) {
            utils.sendColorText(audience, core.getMessage("generic.noconsole"));
            return true;
        }
        String home;
        if (args.length == 0) {
            home = "home";
        } else {
            home = args[0];
        }
        if (!home.matches("^[a-zA-Z0-9]+$")) {
            utils.sendColorText(audience, core.getMessage("generic.alphanumeric",List.of(getCommand().options.get(0).name)));
            return true;
        }
        PlayerData playerData = core.playerManager.getPlayerData((Player) sender);
        if (playerData != null) {
            if (playerData.homes.size() == permissionHandler.getNumberValue("wolfcore.sethome", user).intValue()) {
                utils.sendColorText(audience, core.getMessage("home.limit", List.of(permissionHandler.getNumberValue("wolfcore.sethome", user).toString())));
                return true;
            }
            playerData.homes.put(home, new Home(home, ((Player) sender).getLocation()));
            utils.sendColorText(audience, core.getMessage("home.set", List.of(home)));
        } else {
            utils.sendColorText(audience, core.getMessage("generic.invaliddata"));
        }
        return true;
    }

}
