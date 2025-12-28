package com.wolfco.main.commands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.main.Core;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class Top implements CoreCommand {
    @Override
    public Command getCommand() {
        Command command = new Command().setName("top");
        command.setAccessType(AccessType.PLAYER);

        return command;
    }

    @Inject
    Core core;
    
    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        HumanEntity player = (HumanEntity) sender;
        Location location = player.getLocation();
        World world = player.getWorld();
        Location highestBlock = world.getHighestBlockAt(location).getLocation();
        player.teleport(highestBlock.add(0, 1, 0));

        core.sendMessage(sender,"<#ffaa00>Teleporting...");
        return true;
    }

}
