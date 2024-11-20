package com.wolfco.main.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.Utilities;
import com.wolfco.common.classes.Argument;
import com.wolfco.common.classes.ArgumentType;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.main.Core;

import net.kyori.adventure.audience.Audience;

public class Teleport implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("teleport");
        command.setDescription("Teleport to a player or teleport a player to another player.");
        command.setNode("wolfcore.teleport");
        command.setArguments(Arrays.asList(
                new Argument(ArgumentType.EXCLUSIVEPLAYER, true).setName("PLAYER1"),
                new Argument(ArgumentType.EXCLUSIVEOTHERPLAYER, false).setName("PLAYER2")));

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public Teleport(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String label, String[] args,
            Object[] argumentValues) {
        Player player1 = (Player) argumentValues[0];
        Player player2 = (Player) argumentValues[1];

        Audience senderAudience = core.getAdventure().sender(sender);
        Boolean console = false;

        if (!(sender instanceof Player)) {
            console = true;
        }

        if (player2 == null) {
            if (console) {
                Utilities.sendColorText(senderAudience, core.getMessage("generic.consoleargs", List.of("2")));
                return false;
            }

            if (player1.getUniqueId() == ((Player) sender).getUniqueId()) {
                Utilities.sendColorText(senderAudience, core.getMessage("teleport.self"));
                return false;
            }

            ((Player) sender).teleport(player1);
            Utilities.sendColorText(senderAudience, core.getMessage("teleport.success", List.of(player1.getName())));
        } else {
            if (player1.getUniqueId() == player2.getUniqueId()) {
                Utilities.sendColorText(senderAudience, core.getMessage("teleport.self"));
                return false;
            } else {
                player1.teleport(player2);
            }

            Utilities.sendColorText(senderAudience,
                    core.getMessage("teleport.othersuccess", List.of(player1.getName(), player2.getName())));
            return true;
        }
        return true;
    }
}
