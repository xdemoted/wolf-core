package com.wolfco.main.commands;

import java.util.List;

import org.bukkit.World;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.main.Core;
import com.wolfco.main.classes.Warp;
import com.wolfco.main.classes.customargs.WarpArgument;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class WarpInfo implements CoreCommand {
    @Override
    public Command getCommand() {
        Command command = new Command().setName("warpinfo");
        command.addArguments(new WarpArgument(true));
        return command;
    }

    @Inject
    Core core;
    
    @Override
    public boolean execute(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        Warp warp = (Warp) argumentValues[0];

        World world = core.getServer().getWorld(warp.world);
        String worldName;
        if (world == null) {
            worldName = warp.world.toString();
        } else {
            worldName = world.getName();
        }
        
        core.sendPreset(sender, "warp.info", List.of(warp.name, worldName, String.valueOf(warp.x), String.valueOf(warp.y), String.valueOf(warp.z)));
        return true;
    }
}
