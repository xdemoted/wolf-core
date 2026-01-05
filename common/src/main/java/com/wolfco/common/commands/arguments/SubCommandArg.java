package com.wolfco.common.commands.arguments;

import java.util.HashMap;
import java.util.List;

import com.wolfco.common.classes.ArgumentInterface;
import com.wolfco.common.classes.Command;

import io.papermc.paper.command.brigadier.CommandSourceStack;

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

    public HashMap<String, Command> getCommands() {
        return subcommands;
    }

    public SubCommandArg addCommands(Command... commands) {
        for (Command command : commands) {
            subcommands.put(command.getName(), command);
        }
        return this;
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
    public List<String> getOptions(CommandSourceStack commandStack, String[] args) {
        return subcommands.keySet().stream().toList();
    }

    @Override
    public Object getValue(CommandSourceStack commandStack, String searchValue) {
        return get(searchValue);
    }

    private String node = null;

    @Override
    public ArgumentInterface setNode(String node) {
        this.node = node;
        return this;
    }

    @Override
    public String getNode() {
        return this.node;
    }
}
