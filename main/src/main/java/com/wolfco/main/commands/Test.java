package com.wolfco.main.commands;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.main.Core;

public class Test implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("test");
        command.setNode("wolfcore.test");

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public Test(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        return false; // TODO Add test command logic
    }
}
