package com.wolfco.main.commands;

import org.bukkit.command.CommandSender;

import com.wolfco.common.Utilities;
import com.wolfco.common.classes.Argument;
import com.wolfco.common.classes.ArgumentType;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CommandTypes;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.main.Core;
import com.wolfco.main.handlers.PermissionHandler;

import net.kyori.adventure.audience.Audience;
import net.luckperms.api.model.user.User;

public class Max implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("max");
        command.setDescription("Used to get the max value of a permission.");
        command.setNode("wolfcore.max");
        command.setAccessType(CommandTypes.PLAYER);
        command.addArgument(new Argument(ArgumentType.STRING, false).setName("PERMISSION"));

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
        Audience audience = core.getAdventure().sender(sender);
        User user = core.lp.getUserManager().getUser(sender.getName());
        Utilities.sendColorText(audience, "Max is " + PermissionHandler.getNumberValue(args[0], user));
        return true;
    }

}
