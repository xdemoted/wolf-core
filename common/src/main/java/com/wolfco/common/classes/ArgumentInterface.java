package com.wolfco.common.classes;

import java.util.List;

import org.bukkit.command.CommandSender;

public interface ArgumentInterface {

    default  Boolean isSubcommand() {
        return false;
    }

    abstract String getError();

    abstract Boolean isRequired();

    abstract String getName();

    abstract ArgumentInterface setName(String name);

    abstract List<String> getOptions(CorePlugin core, CommandSender sender, org.bukkit.command.Command bukkitCommand, String[] args);

    abstract Object getValue(CorePlugin core, CommandSender sender, org.bukkit.command.Command bukkitCommand, String searchValue);

}
