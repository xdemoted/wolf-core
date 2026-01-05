package com.wolfco.common.commands.arguments;

import java.util.List;

import com.wolfco.common.classes.ArgumentInterface;

import io.papermc.paper.command.brigadier.CommandSourceStack;

public class BooleanArg implements ArgumentInterface {

    final boolean required;
    String name = "BOOLEAN";

    public BooleanArg(boolean required) {
        this.required = required;
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
        return List.of("true", "false");
    }

    @Override
    public Boolean getValue(CommandSourceStack commandStack, String searchValue) {
        if (searchValue.equalsIgnoreCase("true")) {
            return true;
        } else if (searchValue.equalsIgnoreCase("false")) {
            return false;
        } else {
            throw error("Argument %s requires a valid boolean. Possible values are: [true, false]", name);
        }
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
