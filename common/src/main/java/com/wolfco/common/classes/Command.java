package com.wolfco.common.classes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

public class Command { // TODO Add help command support; add descriptions to commands
    String name;
    String  node;
    String  displayName;
    String  description;

    CommandTypes accessType = CommandTypes.ALL;
    List<ArgumentInterface> options = new ArrayList<>();
    CorePlugin plugin;

    public Command(String name) throws IllegalArgumentException {
        this.name = name;
    }

    public Command setArguments(List<ArgumentInterface> option) {
        this.options = option;

        boolean required = true;

        for (int i = 0; i < options.size() - 1; i++) {
            if (!options.get(i).isRequired()) {
                required = false;
            } else if (options.get(i).isRequired() && !required) {
                throw new IllegalArgumentException("Required arguments must be first");
            }
            if (options.get(i).isSubcommand()) {
                throw new IllegalArgumentException("Subcommand must be the last argument");
            }
        }
        return this;
    }

    public String getName() {
        return name;
    }
    
    public Command setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public Command setNode(String node) {
        this.node = node;
        return this;
    }

    public Command setDescription(String description) {
        this.description = description;
        return this;
    }

    public Command setAccessType(CommandTypes accessType) {
        this.accessType = accessType;
        return this;
    }

    public CommandTypes getAccessType() {
        return accessType;
    }

    public Command addArgument(ArgumentInterface option) {
        options.add(option);
        return this;
    }

    public ArgumentInterface getArgument(int i) {
        return options.get(i);
    }

    public Object getValue(int i, CorePlugin plugin, CommandSender sender, org.bukkit.command.Command bukkitCommand,
            String[] args) throws IllegalArgumentException {
                Object value = options.get(i).getValue(plugin, sender, bukkitCommand, args[i]);

                if (value == null) {
                    throw new IllegalArgumentException("Invalid argument");
                }
    
                return value;
    }

    public Object[] getValues(CorePlugin plugin, CommandSender sender, org.bukkit.command.Command bukkitCommand,
            String[] args) throws IllegalArgumentException {
        Object[] values = new Object[options.size()];

        for (int i = 0; i < options.size(); i++) {
            
            if (args.length <= i) {
                if (options.get(i).isRequired()) {
                    throw new IllegalArgumentException("Missing required argument");
                } else {
                    values[i] = null;
                    continue;
                }
            }

            Object value = options.get(i).getValue(plugin, sender, bukkitCommand, args[i]);

            if (value == null) {
                throw new IllegalArgumentException(options.get(i).getError());
            }

            values[i] = value;
        }

        return values;
    }
}
