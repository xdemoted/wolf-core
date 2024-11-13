package com.wolfco.main.commands;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.kyori.adventure.audience.Audience;

import java.util.ArrayList;
import java.util.List;

import com.wolfco.main.Core;
import com.wolfco.main.classes.customArgs.WarpArgument;
import com.wolfco.common.Utilities;
import com.wolfco.common.classes.ArgumentType;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.Argument;

public class gamemode implements CoreCommandExecutor {
    public Command getCommand() {
        Command command = new Command("gamemode");
        command.setDescription("Used to modify player's gamemode.");
        command.setNode("wolfcore.gamemode");
        command.setArguments(new ArrayList<>() {{
            add(new Argument(ArgumentType.GAMEMODE, true));
            add(new Argument(ArgumentType.PLAYER, false));
        }});

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }
    
    Core core;

    public gamemode(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        Boolean console = false;
        Audience senderAudience = core.getAdventure().sender(sender);
        if (!(sender instanceof Player)) {
            console = true;
        }
        if (console && args.length == 1) {
            Utilities.sendColorText(senderAudience, core.getMessage("generic.consoleargs", List.of("2")));
            return false;
        }
        if (mode == null) {
            Utilities.sendColorText(senderAudience, core.getMessage("gamemode.invalid"));
            return false;
        }
        if (args.length == 1) {
            if (console) {
                Utilities.sendColorText(senderAudience, core.getMessage("generic.consoleargs", List.of("2")));
                return false;
            }
            ((Player) sender).setGameMode(mode);
            Utilities.sendColorText(senderAudience, core.getMessage("gamemode.selfsuccess", List.of(mode.toString())));
            return true;
        } else if (args.length == 2) {
            if (target == null) {
                Utilities.sendColorText(senderAudience, core.getMessage("generic.playernotfound"));
                return false;
            }
            if (target.size() == 1) {
                target.get(0).setGameMode(mode);
                Utilities.sendColorText(senderAudience,
                        core.getMessage("gamemode.othersuccess", List.of(target.get(0).getName(), mode.toString())));
            } else {
                for (Player p : target) {
                    p.setGameMode(mode);
                }
                Utilities.sendColorText(senderAudience, core.getMessage("gamemode.multisuccess",
                        List.of(String.valueOf(target.size()), mode.toString())));
            }
            return true;
        }
        return true;
    }
}
