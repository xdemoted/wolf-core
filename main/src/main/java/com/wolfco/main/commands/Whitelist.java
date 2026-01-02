package com.wolfco.main.commands;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.main.Core;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class Whitelist implements CoreCommand {
    @Override
    public Command getCommand() {
        Command command = new Command().setName("whitelist");
        command.setAccessType(AccessType.PLAYER);

        return command;
    }

    @Inject
    Core core;
    
    @Override
    public boolean onCommand(CommandSourceStack commandStack, String[] args, Object[] argumentValues) {
        return true; // #TODO Implement whitelist command
    }

}
