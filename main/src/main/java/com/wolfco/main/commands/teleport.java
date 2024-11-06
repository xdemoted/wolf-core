package com.wolfco.main.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.kyori.adventure.audience.Audience;

import java.util.ArrayList;
import java.util.List;

import com.wolfco.main.core;
import com.wolfco.common.utils;
import com.wolfco.common.classes.Argument;
import com.wolfco.common.classes.ArgumentType;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;

public class teleport implements CoreCommandExecutor {
    public Command getCommand() {
        return new Command("teleport","wolfcore.teleport", new ArrayList<Argument>() {
            {
                add(new Argument("player", ArgumentType.PLAYER, false));
                add(new Argument("player", ArgumentType.PLAYER, true));
            }
        });
    }

    @Override
    public core fetchCore() {
        return core;
    }
    
    core core;

    public teleport(core core) {
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
        } else if (args.length == 1) {
            Player player1 = getCommand().options.get(0).getExclusivePlayer(core, args[0]);
            if (player1 == null) {
                utils.sendColorText(senderAudience, core.getMessage("generic.playernotfound"));
                return false;
            } else if (player1.getUniqueId() == ((Player) sender).getUniqueId()) {
                utils.sendColorText(senderAudience, core.getMessage("teleport.self"));
                return false;
            }
            ((Player) sender).teleport(player1);
            utils.sendColorText(senderAudience, core.getMessage("teleport.success", List.of(player1.getName())));
        } else if (args.length == 2) {
            List<Player> players = getCommand().options.get(0).getPlayer(core, args[0]);
            Player player2 = getCommand().options.get(1).getExclusivePlayer(core, args[1]);
            if (players == null || player2 == null) {
                utils.sendColorText(senderAudience, core.getMessage("generic.playernotfound"));
                return false;
            }
            for (Player player : players) {
                player.teleport(player2);
            }
            if (players.size() > 1) {
                utils.sendColorText(senderAudience, core.getMessage("teleport.multisuccess", List.of(String.valueOf(players.size()), player2.getName())));
                return true;
            } else {
                utils.sendColorText(senderAudience, core.getMessage("teleport.othersuccess", List.of(players.get(0).getName(),player2.getName())));
                return true;
            }
        }
        return true;
    }
}