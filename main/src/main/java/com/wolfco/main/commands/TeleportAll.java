package com.wolfco.main.commands;

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

public class TeleportAll implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("teleportall");
        command.setDescription("Teleport all players to you.");
        command.setNode("wolfcore.teleportall");
        command.addArgument(new Argument(ArgumentType.EXCLUSIVEPLAYER, false));

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public TeleportAll(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        Audience senderAudience = core.getAdventure().sender(sender);
        Player player1 = (Player) argumentValues[0];

        if (args.length == 0 && sender instanceof Player) {
            core.getServer().getOnlinePlayers().forEach(player -> 
                player.teleport((Player) sender)
            );

            Utilities.sendColorText(senderAudience, core.getMessage("teleportall.success", List.of("you")));

            return true;
        } else if (player1 != null) {
            core.getServer().getOnlinePlayers().forEach(p -> 
                p.teleport(player1)
            );

            Utilities.sendColorText(senderAudience, core.getMessage("teleportall.success", List.of(player1.getName())));
            
            return true;
        }
        
        return false;
    }

}
