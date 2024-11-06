package com.wolfco.common.classes;

import java.util.List;

import org.bukkit.command.CommandSender;

public interface ArgumentInterface {
    abstract ArgumentType getType();

    abstract Boolean isRequired();

    abstract String getName();

    abstract List<String> options(CorePlugin core, CommandSender sender, org.bukkit.command.Command bukkitCommand, String[] args);

    abstract Object getValue(CorePlugin core, CommandSender sender, org.bukkit.command.Command bukkitCommand, String searchValue);

}
