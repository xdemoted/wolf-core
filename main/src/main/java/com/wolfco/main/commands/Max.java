package com.wolfco.main.commands;

import org.bukkit.command.CommandSender;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.common.commands.arguments.StringArg;
import com.wolfco.main.Core;
import com.wolfco.main.utility.PermissionHandler;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import net.luckperms.api.model.user.User;

@Singleton
public class Max implements CoreCommand {
    private final Core core;
    private final PermissionHandler permissionHandler;

    @Inject
    public Max(Core core, PermissionHandler permissionHandler) {
        this.core = core;
        this.permissionHandler = permissionHandler;
    }

    @Override
    public Command getCommand() {
        Command command = new Command().setName("max");
        command.setAccessType(AccessType.PLAYER);
        command.addArguments(new StringArg(true,false,false).setName("PERMISSION"));

        return command;
    }

    @Override
    public boolean onCommand(CommandSourceStack commandStack, String[] args, Object[] argumentValues) {
        CommandSender sender = commandStack.getSender();
        User user = core.getLuckPerms().getUserManager().getUser(sender.getName());
        getMessageUtility().sendMessage(sender, "Max is " + permissionHandler.getNumberValue(args[0], user));
        return true;
    }

}
