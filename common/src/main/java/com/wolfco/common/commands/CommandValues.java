package com.wolfco.common.commands;

import org.bukkit.command.CommandSender;

import org.bukkit.command.Command;

public class CommandValues {
    public CommandSender sender;
    public Command commmand;
    public String label;
    public String[] args;
    public Object[] argumentValues;

    public CommandValues(CommandSender sender, Command command, String label, String[] args,
            Object[] argumentValues) {
        this.sender = sender;
        this.commmand = command;
        this.label = label;
        this.args = args;
        this.argumentValues = argumentValues;
    }
}
