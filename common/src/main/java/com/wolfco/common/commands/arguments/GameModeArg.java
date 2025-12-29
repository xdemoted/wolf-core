package com.wolfco.common.commands.arguments;

import java.util.List;

import org.bukkit.GameMode;

import com.wolfco.common.classes.ArgumentInterface;

import io.papermc.paper.command.brigadier.CommandSourceStack;

public class GameModeArg implements ArgumentInterface {

    final List<String> modes = List.of("survival", "creative", "adventure", "spectator");
    final boolean required;
    String name = "GAMEMODE";

    public GameModeArg(boolean required) {
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
        return modes;
    }

    @Override
    public GameMode getValue(CommandSourceStack commandStack, String searchValue) {
        try {
            return GameMode.valueOf(searchValue.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw error("Argument %s requires a valid gamemode. Possible values are: [%s]", name,
                    String.join(", ", modes));
        }
    }

}
