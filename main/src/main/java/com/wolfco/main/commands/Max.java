package com.wolfco.main.commands;

import org.bukkit.command.CommandSender;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.argumenthandlers.StringArg;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.main.Core;
import com.wolfco.main.handlers.PermissionHandler;

import net.luckperms.api.model.user.User;

public class Max implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("max");
        command.setAccessType(AccessType.PLAYER);
        command.addArguments(new StringArg(true,true,false).setName("PERMISSION"));

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public Max(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        User user = core.getLuckPerms().getUserManager().getUser(sender.getName());
        core.sendMessage(sender, "Max is " + PermissionHandler.getNumberValue(args[0], user));
        return true;
    }

}
