package com.wolfco.common.commands.arguments;

import java.util.List;

import com.wolfco.common.classes.ArgumentInterface;

import io.papermc.paper.command.brigadier.CommandSourceStack;

public class StaticArg implements ArgumentInterface {
    final boolean required;
    String name = "STATIC";
    List<String> options;


    public StaticArg(boolean required, String... options) {
        this.required = required;
        this.options = List.of(options);
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
        return options;
    }

    @Override
    public String getValue(CommandSourceStack commandStack, String searchValue) {
        if (options.contains(searchValue)) {
            return searchValue;
        }

        throw error("Invalid value provided for %s possible values are: [%s]", name, String.join(", ", this.options));
    }
    
}
