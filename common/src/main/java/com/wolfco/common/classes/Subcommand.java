package com.wolfco.common.classes;

import java.util.HashMap;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.wolfco.common.classes.types.ArgumentType;

public class Subcommand implements ArgumentInterface {
    private final HashMap<String, Command> subcommands = new HashMap<>();
    private final boolean required;

    private String name = ArgumentType.SUBCOMMAND.toString();

    public Subcommand(boolean required) {
        this.required = required;
    }

    @Override
    public Boolean isSubcommand() {
        return true;
    }

    @Override
    public Boolean isRequired() {
        return required;
    }

    @Override
    public ArgumentInterface setName(String name) {
        this.name = name;

        return this;
    }

    @Override
    public String getName() {
        return name;
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

    @Override
    public String getError() {
        return String.format("Argument %s requires a valid subcommand.", name);
    }
}
