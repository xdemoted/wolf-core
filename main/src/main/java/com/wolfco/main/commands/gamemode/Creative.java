package com.wolfco.main.commands.gamemode;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.commands.arguments.MultiPlayerArg;
import com.wolfco.main.Core;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class Creative implements CoreCommand {
    final GameMode gamemode = GameMode.CREATIVE;

    @Override
    public Command getCommand() {
        Command command = new Command().setName("gmc");
        command.setNode("wolfcore.gamemode");
        command.addArguments(new MultiPlayerArg(false).includeSender(false));

        return command;
    }

    @Inject
    Core core;

    @SuppressWarnings("unchecked")
    @Override
    public boolean onCommand(CommandSourceStack commandStack, String[] args,
            Object[] argumentValues) {
        Collection<Player> target = (Collection<Player>) argumentValues[0];
        CommandSender sender = commandStack.getSender();

        if (target != null) {
            for (Player tempPlayer : target) {
                tempPlayer.setGameMode(gamemode);
            }

            if (target.size() > 1) {
                core.sendPreset(sender, "gamemode.multisuccess",
                        List.of(String.valueOf(target.size()), gamemode.toString()));
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
