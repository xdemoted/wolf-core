package com.wolfco.main.commands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.main.Core;

public class Top implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("top");

        command.setDescription("Move to the highest block at your location");
        command.setAccessType(AccessType.PLAYER);

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public Top(Core core) {
        this.core = core;
    }

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
