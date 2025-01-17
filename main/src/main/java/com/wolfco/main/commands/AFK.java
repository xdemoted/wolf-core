package com.wolfco.main.commands;

import org.bukkit.command.CommandSender;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.main.Core;
import com.wolfco.main.classes.customargs.HomeArgument;

public class AFK implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("afk");
        command.setAccessType(AccessType.PLAYER);
        command.addArguments(new HomeArgument(true));

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public AFK(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        // TODO Add AFK command logic
        return false;
    }
}
