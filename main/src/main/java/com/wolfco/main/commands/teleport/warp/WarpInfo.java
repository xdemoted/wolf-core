package com.wolfco.main.commands.teleport.warp;

import java.util.List;

import org.bukkit.World;
import org.bukkit.command.CommandSender;

import com.wolfco.common.MessageUtility;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.main.Core;
import com.wolfco.main.commands.arguments.WarpArgument;
import com.wolfco.main.warps.Warp;

import io.papermc.paper.command.brigadier.CommandSourceStack;
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
    MessageUtility messageUtility;

    @Inject
    Core core;
    
    @Override
    public boolean onCommand(CommandSourceStack commandStack, String[] args, Object[] argumentValues) {
        CommandSender sender = commandStack.getSender();
        Warp warp = (Warp) argumentValues[0];

        World world = core.getServer().getWorld(warp.world);
        String worldName;
        if (world == null) {
            worldName = warp.world.toString();
        } else {
            worldName = world.getName();
        }
        
        messageUtility.sendPreset(sender, "warp.info", List.of(warp.name, worldName, String.valueOf(warp.x), String.valueOf(warp.y), String.valueOf(warp.z)));
        return true;
    }
}
