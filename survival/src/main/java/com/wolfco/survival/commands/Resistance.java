package com.wolfco.survival.commands;

import org.bukkit.command.CommandSender;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.common.commands.arguments.StringArg;
import com.wolfco.survival.Core;

import jakarta.inject.Singleton;

@Singleton
public class Resistance implements CoreCommand {
    @Override
    public Command getCommand() {
        Command command = new Command().setName("resistance");
        command.setAccessType(AccessType.PLAYER);
        command.addArguments(
                new StringArg(true).setName("blockName"));
        command.setAccessType(AccessType.ALL);
        return command;
    }


    
    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args,
            Object[] argumentValues) {
        String blockName = (String) argumentValues[0];
        Float resistance = core.blockMap.get(blockName);

        if (resistance == null) {
            sender.sendMessage("Block not found");
            return false;
        }

        sender.sendMessage("Resistance: " + resistance);
        return true;
    }
}
