package com.wolfco.common.classes;

import java.util.HashMap;
import java.util.List;

import org.bukkit.command.CommandSender;

public class Subcommand implements ArgumentInterface {
    private final HashMap<String, Command> subcommands = new HashMap<>();
    private final boolean required;

    public Subcommand(boolean required) {
        this.required = required;
    }

    @Override
    public ArgumentType getType() {
        return ArgumentType.SUBCOMMAND;
    }

    @Override
    public Boolean isRequired() {
        return required;
    }

    @Override
    public String getName() {
        return ArgumentType.SUBCOMMAND.toString();
    }

    public Subcommand add(Command command) {
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
    public List<String> getOptions(CorePlugin core, CommandSender sender,
            org.bukkit.command.Command bukkitCommand, String[] args) {
        return subcommands.keySet().stream().toList();
    }

    @Override
    public Object getValue(CorePlugin core, CommandSender sender, org.bukkit.command.Command bukkitCommand,
            String searchValue) {
        return get(searchValue);
    }
}
