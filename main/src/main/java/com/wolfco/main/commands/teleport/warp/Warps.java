package com.wolfco.main.commands.teleport.warp;

import org.bukkit.command.CommandSender;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.main.Core;

import io.papermc.paper.command.brigadier.CommandSourceStack;
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
    public boolean onCommand( CommandSourceStack commandStack,
            String[] args, Object[] argumentValues) {
        CommandSender sender = commandStack.getSender();
        core.sendMessage(sender, "<#ffaa00>Warps:");
        for (String key : core.getWarps().getRoutesAsStrings(false)) {
            core.sendMessage(sender,
                    "<#ffaa00> - <#ffff00><click:run_command:/warp " + key + ">" + key + "</click>");
        }
        return true;
    }
}
