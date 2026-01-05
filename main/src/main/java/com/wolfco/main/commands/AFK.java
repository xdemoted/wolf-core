package com.wolfco.main.commands;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.main.commands.arguments.HomeArgument;

import io.papermc.paper.command.brigadier.CommandSourceStack;
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
    public boolean onCommand(CommandSourceStack commandStack, String[] args, Object[] argumentValues) {
        // TODO Add AFK command logic
        return false;
    }
}
