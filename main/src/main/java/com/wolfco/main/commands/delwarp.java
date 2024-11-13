package com.wolfco.main.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import com.wolfco.main.core;
import com.wolfco.main.classes.customArgs.WarpArg;
import com.wolfco.common.utils;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;

public class delwarp implements CoreCommandExecutor {
    @Override
    public Command getCommand() {
        Command command = new Command("delwarp");
        command.setDescription("Used to delete warps");
        command.setNode("wolfcore.delwarp");
        command.setOptions(new ArrayList<>() {{
            add(new WarpArg(true));
        }});

        return command;
    }

    @Override
    public core fetchCore() {
        return core;
    }
    
    core core;
    public delwarp(core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        boolean result = core.warps.remove(args[0]);
        if (result) {
            utils.sendColorText(core.getAdventure().sender(sender), core.getMessage("warp.deleted", List.of(args[0])));
        } else {
            utils.sendColorText(core.getAdventure().sender(sender), core.getMessage("warp.notfound", List.of(args[0])));
        }
        try {
            core.warps.save();
        } catch (Exception e) {
        }
        return result;
    }
    
}
