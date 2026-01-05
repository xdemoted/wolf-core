package com.wolfco.survival.commands;

import org.bukkit.command.CommandSender;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.common.commands.arguments.StringArg;
import com.wolfco.survival.Core;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import jakarta.inject.Singleton;

@Singleton
public class Resistance implements CoreCommand {
    private final Core core;

    public Resistance(Core core) {
        this.core = core;
    }

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
    public boolean onCommand(CommandSourceStack commandStack, String[] args,
            Object[] argumentValues) {
        CommandSender sender = commandStack.getSender();
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
