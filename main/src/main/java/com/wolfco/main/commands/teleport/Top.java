package com.wolfco.main.commands.teleport;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.classes.types.AccessType;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import jakarta.inject.Singleton;

@Singleton
public class Top implements CoreCommand {
    @Override
    public Command getCommand() {
        Command command = new Command().setName("top");
        command.setAccessType(AccessType.PLAYER);

        return command;
    }
    
    @Override
    public boolean onCommand(CommandSourceStack commandStack, String[] args, Object[] argumentValues) {
        CommandSender sender = commandStack.getSender();
        HumanEntity player = (HumanEntity) sender;
        Location location = player.getLocation();
        World world = player.getWorld();
        Location highestBlock = world.getHighestBlockAt(location).getLocation();
        player.teleport(highestBlock.add(0, 1, 0));

        getMessageUtility().sendMessage(sender,"<#ffaa00>Teleporting...");
        return true;
    }

}
