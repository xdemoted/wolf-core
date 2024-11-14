package com.wolfco.main.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.Utilities;
import com.wolfco.common.classes.Argument;
import com.wolfco.common.classes.ArgumentType;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.main.Core;

import net.kyori.adventure.audience.Audience;

public class gamemode implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("gamemode");
        command.setDescription("Used to modify player's gamemode.");
        command.setNode("wolfcore.gamemode");
        command.setArguments(new ArrayList<>() {
            {
                add(new Argument(ArgumentType.GAMEMODE, true));
                add(new Argument(ArgumentType.OTHERPLAYER, false));
            }
        });

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
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String label, String[] args, Object[] argumentValues) {
        GameMode mode = (GameMode) argumentValues[0];
        Collection<Player> target = (Collection<Player>) argumentValues[1];

        Boolean console = (sender instanceof ConsoleCommandSender);
        Audience senderAudience = core.getAdventure().sender(sender);

        if (console && args.length == 1) {
            Utilities.sendColorText(senderAudience, core.getMessage("generic.consoleargs", List.of("2")));
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
        } else if (target instanceof Collection) { // TODO implement multi-targeting
            if (target.size() == 1) {
                Utilities.sendColorText(senderAudience,
                        core.getMessage("gamemode.othersuccess", List.of(target.iterator().next().getName(), mode.toString())));
            } else {
                Utilities.sendColorText(senderAudience, core.getMessage("gamemode.multisuccess",
                        Arrays.asList(String.valueOf(target.size()), mode.toString())));
            }

            for (Player p : target) {
                p.setGameMode(mode);
            }

            return true;
        }
        return true;
    }
}
