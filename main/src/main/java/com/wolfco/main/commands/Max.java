package com.wolfco.main.commands;

import org.bukkit.command.CommandSender;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.common.commands.arguments.StringArg;
import com.wolfco.main.Core;
import com.wolfco.main.handlers.PermissionHandler;

import net.luckperms.api.model.user.User;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class Max implements CoreCommand {
    @Override
    public Command getCommand() {
        Command command = new Command().setName("max");
        command.setAccessType(AccessType.PLAYER);
        command.addArguments(new StringArg(true,true,false).setName("PERMISSION"));

        return command;
    }

    @Inject
    Core core;
    
    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        User user = core.getLuckPerms().getUserManager().getUser(sender.getName());
        core.sendMessage(sender, "Max is " + PermissionHandler.getNumberValue(args[0], user));
        return true;
    }

}
