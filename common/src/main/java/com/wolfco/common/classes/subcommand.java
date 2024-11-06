package com.wolfco.common.classes;

import java.util.HashMap;
import java.util.List;

import org.bukkit.command.CommandSender;

public class subcommand implements ArgumentInterface {
    private HashMap<String, Command> subcommands = new HashMap<String, Command>();
    private boolean required;

    public subcommand(boolean required) {
        this.required = required;
    }

    public ArgumentType getType() {
        return ArgumentType.SUBCOMMAND;
    }

    public Boolean isRequired() {
        return required;
    }

    public String getName() {
        return ArgumentType.SUBCOMMAND.toString();
    }

    public subcommand add(Command command) {
        subcommands.put(command.name, command);
        return this;
    }
    
    public Command get(String name) {
        for (Command command : subcommands.values()) {
            if (command.name.equalsIgnoreCase(name)) {
                return command;
            }
        }
        return null;
    }

    @Override
    public List<String> options(CorePlugin core, CommandSender sender,
            org.bukkit.command.Command bukkitCommand, String[] args) {
        return subcommands.keySet().stream().toList();
    }

    @Override
    public Object getValue(CorePlugin core, CommandSender sender, org.bukkit.command.Command bukkitCommand,
            String searchValue) {
        return get(searchValue);
    }
}
