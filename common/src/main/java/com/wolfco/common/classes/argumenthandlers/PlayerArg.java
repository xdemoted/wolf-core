package com.wolfco.common.classes.argumenthandlers;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.wolfco.common.classes.ArgumentInterface;
import com.wolfco.common.classes.CorePlugin;

public class PlayerArg implements ArgumentInterface {
    final boolean required;
    String name = "PLAYER";

    public PlayerArg(boolean required) {
        this.required = required;
    }

    @Override
    public String getError() {
        return String.format("Argument %s requires a valid player.", name);
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOptions'");
    }

    @Override
    public Object getValue(CorePlugin core, CommandSender sender, Command bukkitCommand, String searchValue) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getValue'");
    }
    
}
