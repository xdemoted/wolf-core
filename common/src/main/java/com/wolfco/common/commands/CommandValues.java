package com.wolfco.common.commands;

import io.papermc.paper.command.brigadier.CommandSourceStack;

public class CommandValues {
    public CommandSourceStack commandStack;
    public String[] args;
    public Object[] argumentValues;

    public CommandValues(CommandSourceStack commandStack, String[] args,
            Object[] argumentValues) {
        this.commandStack = commandStack;
        this.args = args;
        this.argumentValues = argumentValues;
    }
}
