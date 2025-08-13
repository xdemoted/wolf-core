package com.wolfco.survival.commands;

import org.bukkit.command.CommandSender;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.argumenthandlers.StringArg;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.survival.Core;

public class Resistance implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("resistance");
        command.setAccessType(AccessType.PLAYER);
        command.addArguments(
                new StringArg(true).setName("blockName"));
        command.setAccessType(AccessType.ALL);
        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public Resistance(Core core) {
        this.core = core;
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
