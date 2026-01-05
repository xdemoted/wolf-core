package com.wolfco.common.classes;

import java.util.List;

import io.papermc.paper.command.brigadier.CommandSourceStack;

public interface ArgumentInterface {
    default IllegalArgumentException error(String message, Object... args) {
        if (args == null || args.length == 0) {
            return new IllegalArgumentException(message);
        }
        return new IllegalArgumentException(String.format(message, args));
    }

    abstract Boolean isRequired();

    abstract String getName();

    abstract ArgumentInterface setName(String name);

    abstract List<String> getOptions(CommandSourceStack commandStack, String[] args);

    abstract Object getValue(CommandSourceStack commandStack, String searchValue) throws IllegalArgumentException;

    abstract ArgumentInterface setNode(String node);

    abstract String getNode();
}
