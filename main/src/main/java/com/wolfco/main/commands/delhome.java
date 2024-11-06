package com.wolfco.main.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.kyori.adventure.audience.Audience;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.main.core;
import com.wolfco.main.classes.Home;
import com.wolfco.main.classes.PlayerData;
import com.wolfco.main.classes.customArgs.HomeArg;
import com.wolfco.common.utils;

import java.util.ArrayList;
import java.util.List;

public class delhome implements CoreCommandExecutor {
    @Override
    public Command getCommand() {
        Command command = new Command("delhome");
        command.setDescription("Used to delete homes");
        command.setNode("wolfcore.delhome");
        command.setOptions(new ArrayList<>() {{
            add(new HomeArg(true));
        }});

        return command;
    }

    @Override
    public core fetchCore() {
        return core;
    }

    core core;

    public delhome(core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        Audience audience = core.adventure().sender(sender);
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
            utils.sendColorText(audience,
                    core.getMessage("generic.alphanumeric", List.of(getCommand().getArgument(0).getName())));
            return true;
        }
        PlayerData playerData = core.playerManager.getPlayerData((Player) sender);
        if (playerData != null) {
            Home result = playerData.homes.remove(home);
            if (result == null) {
                utils.sendColorText(audience, core.getMessage("home.notfound", List.of(home)));
                return true;
            }
            utils.sendColorText(audience, core.getMessage("home.deleted", List.of(home)));
        }
        return true;
    }
}
