package com.wolfco.main.commands;

import java.util.List;

import org.bukkit.World;

import com.wolfco.common.Utilities;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.main.Core;
import com.wolfco.main.classes.Warp;
import com.wolfco.main.classes.customArgs.WarpArgument;

import net.kyori.adventure.audience.Audience;

public class warpinfo implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("warpinfo");
        command.setDescription("Get information about a warp");
        command.setNode("wolfcore.warpinfo");
        command.addArgument(new WarpArgument(true));
        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public warpinfo(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        Warp warp = (Warp) argumentValues[0];
        Audience audience = core.getAdventure().sender(sender);

        World world = core.getServer().getWorld(warp.world);
        String worldName;
        if (world == null) {
            worldName = warp.world.toString();
        } else {
            worldName = world.getName();
        }
        
        Utilities.sendColorText(audience, core.getMessage("warp.info", List.of(warp.name, worldName, String.valueOf(warp.x), String.valueOf(warp.y), String.valueOf(warp.z))));
        return true;
    }
}
