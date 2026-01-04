package com.wolfco.common.commands.arguments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.wolfco.common.classes.ArgumentInterface;
import com.wolfco.common.classes.Command;

import io.papermc.paper.command.brigadier.CommandSourceStack;

public class ImplicitSubCommandArg implements ArgumentInterface {
    private final HashMap<ArgumentInterface, Command> subcommands = new HashMap<>();
    private final boolean required;

    private String name = "SUBCOMMAND";

    public ImplicitSubCommandArg(boolean required) {
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

    public ImplicitSubCommandArg add(ArgumentInterface argument, Command command) {
        subcommands.put(argument, command);
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
        List<String> options = new ArrayList<>();

        subcommands.keySet().stream().forEach(argument -> {
            options.addAll(argument.getOptions(commandStack, args));
        });

        return options;
    }

    @Override
    public Object getValue(CommandSourceStack commandStack, String searchValue) {
        return get(searchValue);
    }
}
