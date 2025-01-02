package com.wolfco.main.commands;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Argument;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.types.ArgumentType;
import com.wolfco.main.Core;

public class Gamemode implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("gamemode");
        command.setDescription("Used to modify player's gamemode.");
        command.setArguments(Arrays.asList(
                new Argument(ArgumentType.GAMEMODE, true),
                new Argument(ArgumentType.OTHERPLAYER, false)
        ));

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public Gamemode(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String label, String[] args, Object[] argumentValues) {
        GameMode mode = (GameMode) argumentValues[0];

        @SuppressWarnings("unchecked")
        Collection<Player> target = (Collection<Player>) argumentValues[1];

        Boolean console = (sender instanceof ConsoleCommandSender);

        if (console && args.length == 1) {
            core.sendPreset(sender, "generic.consoleargs", List.of("2"));
            return false;
        }

        if (args.length == 1) {

            if (console) {
                core.sendPreset(sender, "generic.consoleargs", List.of("2"));
                return false;
            }

            ((Player) sender).setGameMode(mode);
            core.sendPreset(sender, "gamemode.selfsuccess", List.of(mode.toString()));
            return true;
        } else if (target instanceof Collection) {
            if (target.size() == 1) {
                core.sendPreset(sender, "gamemode.othersuccess", List.of(target.iterator().next().getName(), mode.toString()));
            } else {
                core.sendPreset(sender, "gamemode.multisuccess", List.of(String.valueOf(target.size()), mode.toString()));
            }

            for (Player p : target) {
                p.setGameMode(mode);
            }

            return true;
        }
        return true;
    }
}
