package com.wolfco.main.commands;

import java.util.Collection;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.commands.arguments.MultiPlayerArg;
import com.wolfco.common.commands.arguments.WorldArg;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import jakarta.inject.Singleton;

@Singleton
public class WorldCMD implements CoreCommand {
    @Override
    public Command getCommand() {
        Command command = new Command().setName("world");
        command.addArguments(new WorldArg(true), new MultiPlayerArg(false));

        return command;
    }
    
    @Override
    public boolean onCommand(CommandSourceStack commandStack,
            String[] args, Object[] argumentValues) {
        CommandSender sender = commandStack.getSender();
        World world = (World) argumentValues[0];

        @SuppressWarnings("unchecked")
        Collection<? extends Player> players = (Collection<? extends Player>) argumentValues[1];

        if (players == null) {
            ((Player) sender).teleport(world.getSpawnLocation());
        } else {
            for (Player player : players) {
                player.teleport(world.getSpawnLocation());
            }
        }

        getMessageUtility().sendMessage(sender, "<#ffaa00>Teleported %s to world %s");
        return true;
    }
}
