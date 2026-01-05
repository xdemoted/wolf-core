package com.wolfco.common.commands.arguments;

import java.util.List;

import com.wolfco.common.classes.ArgumentInterface;

import io.papermc.paper.command.brigadier.CommandSourceStack;

public class StringArg implements ArgumentInterface {
    final boolean required;
    boolean alphanum = false;
    boolean allowSpaces = false;

    String name = "STRING";

    public StringArg(boolean required, boolean alphanum) {
        this.required = required;
        this.alphanum = alphanum;
    }

    public StringArg(boolean required) {
        this.required = required;
    }

    public StringArg(boolean required, boolean alphanum, boolean allowSpaces) {
        this.required = required;
        this.alphanum = alphanum;
        this.allowSpaces = allowSpaces;
    }

    @Override
    public Boolean isRequired() {
        return required;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ArgumentInterface setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public List<String> getOptions(CommandSourceStack commandStack, String[] args) {
        return List.of("\" \"");
    }

    @Override
    public String getValue(CommandSourceStack commandStack, String searchValue)
            throws IllegalArgumentException {
        if (alphanum && !searchValue.matches("^[a-zA-Z0-9]*$")) {
            throw error("Invalid value provided for %s, must be alphanumeric", name);
        } else if (!allowSpaces && searchValue.contains(" ")) {
            throw error("Invalid value provided for %s, must not contain spaces", name);
        }

        return searchValue;
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
