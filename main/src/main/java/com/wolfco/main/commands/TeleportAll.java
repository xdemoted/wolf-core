package com.wolfco.main.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.commands.arguments.PlayerArg;
import com.wolfco.main.Core;
import com.wolfco.main.utility.FontUtil;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class TeleportAll implements CoreCommand {
    @Override
    public Command getCommand() {
        Command command = new Command().setName("teleportall");
        command.addArguments(new PlayerArg(false).includeSender(false).setName("PLAYER"));

        return command;
    }

    @Inject
    Core core;
    
    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        Player player1 = (Player) argumentValues[0];

        if (args.length == 0 && sender instanceof Player) {
            core.getServer().getOnlinePlayers().forEach(player -> 
                player.teleport((Player) sender)
            );

            core.sendPreset(sender, "teleportall.success", List.of("you"));

            return true;
        } else if (player1 != null) {
            core.getServer().getOnlinePlayers().forEach(p -> 
                p.teleport(player1)
            );

            core.sendPreset(sender, "teleportall.success", List.of(FontUtil.getPlayerTag(player1)));
            
            return true;
        }
        
        return false;
    }

}
