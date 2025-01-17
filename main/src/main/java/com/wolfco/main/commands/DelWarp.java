package com.wolfco.main.commands;

import java.io.IOException;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.main.Core;
import com.wolfco.main.classes.customargs.WarpArgument;

public class DelWarp implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("delwarp");
        command.addArguments(new WarpArgument(true));

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public DelWarp(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        boolean result = core.getWarps().remove(args[0]);
        if (result) {
            core.sendPreset(sender, "warp.deleted", List.of(args[0]));
        } else {
            core.sendPreset(sender, "warp.notfound", List.of(args[0]));
        }
        
        try {
            core.getWarps().save();
        } catch (IOException e) {
            sender.sendMessage("An error occurred while saving warps");
        }

        return result;
    }

}
