package com.wolfco.main.commands;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.main.Core;

import jakarta.inject.Singleton;

@Singleton
public class Test implements CoreCommand {
    @Override
    public Command getCommand() {
        Command command = new Command().setName("test");
        command.setNode("wolfcore.test");

        return command;
    }

    @Override
    public boolean execute(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        return false; // TODO Add test command logic
    }
}
