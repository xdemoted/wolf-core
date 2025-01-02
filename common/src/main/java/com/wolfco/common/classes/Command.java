package com.wolfco.common.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.wolfco.common.classes.types.AccessType;

public class Command { // TODO Add help command support; add descriptions to commands

    String name;
    String node;
    String displayName;
    String description;

    AccessType accessType = AccessType.ALL;
    List<ArgumentInterface> options = new ArrayList<>();

    public Command(String name) throws IllegalArgumentException {
        this.name = name;
        node = "wolfcore." + name;
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

    public Command setAccessType(AccessType accessType) {
        this.accessType = accessType;
        return this;
    }

    public AccessType getAccessType() {
        return accessType;
    }

    public Command addArguments(ArgumentInterface... options) {
        this.options.addAll(Arrays.asList(options));

        boolean required = true;

        for (int i = 0; i < this.options.size() - 1; i++) {
            if (!this.options.get(i).isRequired()) {
                required = false;
            } else if (this.options.get(i).isRequired() && !required) {
                throw new IllegalArgumentException("Required arguments must be first");
            }
            if (this.options.get(i).isSubcommand()) {
                throw new IllegalArgumentException("Subcommand must be the last argument");
            }
        }

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
                throw new IllegalArgumentException("Argument " + options.get(i).getName() + " returned null without error!");
            }

            values[i] = value;
        }

        return values;
    }
}
