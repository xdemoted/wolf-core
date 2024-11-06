package com.wolfco.main.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;

import com.wolfco.main.core;
import com.wolfco.main.classes.Warp;
import com.wolfco.common.utils;
import com.wolfco.common.classes.Argument;
import com.wolfco.common.classes.ArgumentType;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.argument;

public class warpinfo implements CoreCommandExecutor {
    @Override
    public Command getCommand() {
        return new Command("warpinfo","wolfcore.warpinfo", new ArrayList<>() {{
            new argument("warp", ArgumentType.WARP, false);
        }});
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
        Warp warp = getCommand().options.get(0).getWarp(core.warps, args[0]);
        if (warp == null) {
            utils.sendColorText(core.adventure().sender(sender), core.getMessage("warp.notfound", List.of(args[0])));
            return false;
        }
        World world = core.getServer().getWorld(warp.world);
        String worldName;
        if (world == null) {
            worldName = warp.world.toString();
        } else {
            worldName = world.getName();
        }
        utils.sendColorText(core.adventure().sender(sender), core.getMessage("warp.info", List.of(warp.name, worldName, String.valueOf(warp.x), String.valueOf(warp.y), String.valueOf(warp.z))));
        return true;
    }
}
