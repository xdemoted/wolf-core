package com.wolfco.main.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.Utilities;
import com.wolfco.common.classes.Argument;
import com.wolfco.common.classes.ArgumentType;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.main.Core;
import com.wolfco.main.handlers.PermissionHandler;

import net.kyori.adventure.audience.Audience;
import net.luckperms.api.model.user.User;

public class max implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("max");
        command.setDescription("Used to get the max value of a permission.");
        command.setNode("wolfcore.max");
        command.addArgument(new Argument(ArgumentType.ALPHANUMERICSTRING, false));

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public max(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        Audience audience = core.getAdventure().sender(sender);
        User user = core.lp.getUserManager().getUser(sender.getName());
        if (!(sender instanceof Player)) {
            Utilities.sendColorText(audience, core.getMessage("generic.noconsole"));
            return true;
        }
        Utilities.sendColorText(audience, "Max is " + PermissionHandler.getNumberValue(args[0], user));
        return true;
    }

}
