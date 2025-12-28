package com.wolfco.main.commands;

import org.bukkit.command.CommandSender;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.main.Core;
import com.wolfco.main.classes.customargs.HomeArgument;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class AFK implements CoreCommand {
    @Override
    public Command getCommand() {
        Command command = new Command().setName("afk");
        command.setAccessType(AccessType.PLAYER);
        command.addArguments(new HomeArgument(true));

        return command;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        // TODO Add AFK command logic
        return false;
    }
}
