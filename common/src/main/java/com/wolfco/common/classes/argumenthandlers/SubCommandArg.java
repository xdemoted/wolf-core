package com.wolfco.common.classes.argumenthandlers;

import java.util.HashMap;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.wolfco.common.classes.ArgumentInterface;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CorePlugin;

public class SubCommandArg implements ArgumentInterface {
    private final HashMap<String, Command> subcommands = new HashMap<>();
    private final boolean required;

    private String name = "SUBCOMMAND";

    public SubCommandArg(boolean required) {
        this.required = required;
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

    public SubCommandArg add(Command command) {
        subcommands.put(command.getName(), command);
        return this;
    }
    
    public Command get(String name) {
        for (Command command : subcommands.values()) {
            if (command.getName().equalsIgnoreCase(name)) {
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
