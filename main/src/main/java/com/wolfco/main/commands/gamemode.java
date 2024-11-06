package com.wolfco.main.commands;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.kyori.adventure.audience.Audience;

import java.util.ArrayList;
import java.util.List;

import com.wolfco.main.core;
import com.wolfco.main.classes.customArgs.WarpArg;
import com.wolfco.common.utils;
import com.wolfco.common.classes.ArgumentType;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.argument;

public class gamemode implements CoreCommandExecutor {
    public Command getCommand() {
        Command command = new Command("gamemode");
        command.setDescription("Used to modify player's gamemode.");
        command.setNode("wolfcore.gamemode");
        command.setOptions(new ArrayList<>() {{
            add(new argument(ArgumentType.GAMEMODE, true));
        }});

        return command;
    }

    @Override
    public core fetchCore() {
        return core;
    }
    
    core core;

    public gamemode(core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        Boolean console = false;
        Audience senderAudience = core.adventure().sender(sender);
        if (!(sender instanceof Player)) {
            console = true;
        }
        if (console && args.length == 1) {
            utils.sendColorText(senderAudience, core.getMessage("generic.consoleargs", List.of("2")));
            return false;
        }
        if (mode == null) {
            utils.sendColorText(senderAudience, core.getMessage("gamemode.invalid"));
            return false;
        }
        if (args.length == 1) {
            if (console) {
                utils.sendColorText(senderAudience, core.getMessage("generic.consoleargs", List.of("2")));
                return false;
            }
            ((Player) sender).setGameMode(mode);
            utils.sendColorText(senderAudience, core.getMessage("gamemode.selfsuccess", List.of(mode.toString())));
            return true;
        } else if (args.length == 2) {
            if (target == null) {
                utils.sendColorText(senderAudience, core.getMessage("generic.playernotfound"));
                return false;
            }
            if (target.size() == 1) {
                target.get(0).setGameMode(mode);
                utils.sendColorText(senderAudience,
                        core.getMessage("gamemode.othersuccess", List.of(target.get(0).getName(), mode.toString())));
            } else {
                for (Player p : target) {
                    p.setGameMode(mode);
                }
                utils.sendColorText(senderAudience, core.getMessage("gamemode.multisuccess",
                        List.of(String.valueOf(target.size()), mode.toString())));
            }
            return true;
        }
        return true;
    }
}
