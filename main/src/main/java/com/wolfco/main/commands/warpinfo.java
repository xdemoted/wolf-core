package com.wolfco.main.commands;

import java.util.List;

import org.bukkit.World;

import com.wolfco.main.core;
import com.wolfco.main.classes.Warp;
import com.wolfco.main.classes.customArgs.WarpArg;
import com.wolfco.common.utils;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;

public class warpinfo implements CoreCommandExecutor {
    @Override
    public Command getCommand() {
        Command command = new Command("warpinfo");
        command.setDescription("Gets information about a warp.");
        command.setNode("wolfcore.warpinfo");
        command.addOption(new WarpArg(true));

        return command;
    }

    @Override
    public core fetchCore() {
        return core;
    }
    
    core core;
    public warpinfo(core core) {
        this.core = core;
    }

    @Override
    public boolean execute(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        Warp warp;

        try {
            warp = (Warp) getArgument(0).getValue(core, sender, command, args[0]);
        } catch (Exception e) {
            return false;
        }
        
        if (warp == null) {
            utils.sendColorText(core.getAdventure().sender(sender), core.getMessage("warp.notfound", List.of(args[0])));
            return false;
        }
        World world = core.getServer().getWorld(warp.world);
        String worldName;
        if (world == null) {
            worldName = warp.world.toString();
        } else {
            worldName = world.getName();
        }
        utils.sendColorText(core.getAdventure().sender(sender), core.getMessage("warp.info", List.of(warp.name, worldName, String.valueOf(warp.x), String.valueOf(warp.y), String.valueOf(warp.z))));
        return true;
    }
}
