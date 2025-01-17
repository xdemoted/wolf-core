package com.wolfco.common.classes.argumenthandlers;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.wolfco.common.classes.ArgumentInterface;
import com.wolfco.common.classes.CorePlugin;

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
    public List<String> getOptions(CorePlugin core, CommandSender sender, Command bukkitCommand, String[] args) {
        return options;
    }

    @Override
    public String getValue(CorePlugin core, CommandSender sender, Command bukkitCommand, String searchValue) {
        if (options.contains(searchValue)) {
            return searchValue;
        }

        throw error("Invalid value provided for %s possible values are: [%s]", name, String.join(", ", this.options));
    }
    
}
