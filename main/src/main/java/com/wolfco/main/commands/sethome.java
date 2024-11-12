package com.wolfco.main.commands;

import com.wolfco.main.Core;
import com.wolfco.main.classes.Home;
import com.wolfco.main.classes.PlayerData;
import com.wolfco.main.handlers.PermissionHandler;
import com.wolfco.common.Utilities;
import com.wolfco.common.classes.Argument;
import com.wolfco.common.classes.ArgumentType;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;

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
    public Core fetchCore() {
        return core;
    }
    
    Core core;
    public sethome(Core core) {
        this.core = core;
    }
    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        Audience audience = core.getAdventure().sender(sender);
        User user = core.lp.getUserManager().getUser(sender.getName());
        if (!(sender instanceof Player)) {
            Utilities.sendColorText(audience, core.getMessage("generic.noconsole"));
            return true;
        }
        String home;
        if (args.length == 0) {
            home = "home";
        } else {
            home = args[0];
        }
        if (!home.matches("^[a-zA-Z0-9]+$")) {
            Utilities.sendColorText(audience, core.getMessage("generic.alphanumeric",List.of(getCommand().options.get(0).name)));
            return true;
        }
        PlayerData playerData = core.playerManager.getPlayerData((Player) sender);
        if (playerData != null) {
            if (playerData.homes.size() == PermissionHandler.getNumberValue("wolfcore.sethome", user).intValue()) {
                Utilities.sendColorText(audience, core.getMessage("home.limit", List.of(PermissionHandler.getNumberValue("wolfcore.sethome", user).toString())));
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
