package com.wolfco.main.commands;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.argumenthandlers.MultiPlayerArg;
import com.wolfco.main.Core;

public class GamemodeAlias implements CoreCommandExecutor {

    final HashMap<String, GameMode> gamemodes = new HashMap<>();

    public GamemodeAlias() {
        gamemodes.put("gms", GameMode.SURVIVAL);
        gamemodes.put("gmc", GameMode.CREATIVE);
        gamemodes.put("gma", GameMode.ADVENTURE);
        gamemodes.put("gmsp", GameMode.SPECTATOR);
    }

    @Override
    public Command getCommand() {
        Command command = new Command("gms");
        command.setNode("wolfcore.gamemode");
        command.addArguments(new MultiPlayerArg(false).includeSender(false));

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public GamemodeAlias(@Nonnull Core core) {
        this.core = core;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        Collection<Player> target = (Collection<Player>) argumentValues[0];

        GameMode gamemode = switch (alias) {
            case "gms" -> GameMode.SURVIVAL;
            case "gmc" -> GameMode.CREATIVE;
            case "gma" -> GameMode.ADVENTURE;
            case "gmsp" -> GameMode.SPECTATOR;
            default -> null;
        };

        if (gamemode == null) {
            core.sendPreset(sender, core.getMessage("gamemode.invalid"));
            return false;
        }

        if (target != null) {
            for (Player tempPlayer : target) {
                tempPlayer.setGameMode(gamemode);
            }

            if (target.size() > 1) {
                core.sendPreset(sender, "gamemode.multisuccess", List.of(String.valueOf(target.size()), gamemode.toString()));
                return true;
            }

            core.sendPreset(sender, "gamemode.othersuccess", List.of(gamemode.toString()));
            return true;
        } else {
            if (sender instanceof Player player) {
                player.setGameMode(gamemode);
                core.sendPreset(sender, "gamemode.selfsuccess", List.of(gamemode.toString()));
                return false;
            } else if (sender instanceof ConsoleCommandSender) {
                core.sendPreset(sender, "generic.consoleargs", List.of("1"));
                return false;
            }
        }
        return false;
    }

}
