package com.wolfco.common.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.wolfco.common.classes.types.AccessType;
import com.wolfco.common.commands.arguments.SubCommandArg;

import io.avaje.inject.Component;
import jakarta.inject.Inject;

@Component
public class Command { // TODO Add help command support; add descriptions to commands
    @Inject
    CorePlugin core;

    String name;
    String node = null;

    AccessType accessType = AccessType.ALL;
    List<ArgumentInterface> options = new ArrayList<>();

    public Command() throws IllegalArgumentException {
    }

    public Command setName(String name) {
        this.name = name;
        node = "wolfcore." + name;
        return this;
    }

    public String getName() {
        return name;
    }

    public void usePermissions(boolean use) {
        if (use) {
            node = "wolfcore." + name;
        } else {
            node = null;
        }
    }

    public Command setNode(String node) {
        this.node = node;
        return this;
    }

    public String getNode() {
        return node;
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
            if (this.options.get(i) instanceof SubCommandArg) {
                throw new IllegalArgumentException("Subcommand must be the last argument");
            }
        }

        return this;
    }

    public ArgumentInterface getArgument(int i) {
        return options.get(i);
    }

    public List<ArgumentInterface> getArguments() {
        return options;
    }

    public Object getValue(int i, CommandSender sender, org.bukkit.command.Command bukkitCommand,
            String[] args) throws IllegalArgumentException {
        Object value = options.get(i).getValue(core, sender, bukkitCommand, args[i]);

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
                throw new IllegalArgumentException(
                        "Argument " + options.get(i).getName() + " returned null without error!");
            }

            values[i] = value;
        }

        return values;
    }
}
