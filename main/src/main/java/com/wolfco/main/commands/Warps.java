package com.wolfco.main.commands;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.main.Core;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class Warps implements CoreCommand {
    @Override
    public Command getCommand() {
        Command command = new Command().setName("warps");

        return command;
    }

    @Inject
    Core core;
    
    @Override
    public boolean execute(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String alias,
            String[] args, Object[] argumentValues) {
        core.sendMessage(sender, "<#ffaa00>Warps:");
        for (String key : core.getWarps().getRoutesAsStrings(false)) {
            core.sendMessage(sender,
                    "<#ffaa00> - <#ffff00><click:run_command:/warp " + key + ">" + key + "</click>");
        }
        return true;
    }
}
