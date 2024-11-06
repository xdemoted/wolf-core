package com.wolfco.main.commands;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.kyori.adventure.audience.Audience;
import net.luckperms.api.model.user.User;
import com.wolfco.main.core;
import com.wolfco.common.utils;
import com.wolfco.common.classes.Argument;
import com.wolfco.common.classes.ArgumentType;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.main.utilities.permissionHandler;

public class max implements CoreCommandExecutor {
    @Override
    public Command getCommand() {
        return new Command("max","wolfcore.max", new ArrayList<>() {{
            add(new Argument("node", ArgumentType.STRING, false));
        }});
    }

    @Override
    public core fetchCore() {
        return core;
    }

    core core;
    public max(core core) {
        this.core = core;
    }
    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        Audience audience = core.adventure().sender(sender);
        User user = core.lp.getUserManager().getUser(sender.getName());
        if (!(sender instanceof Player)) {
            utils.sendColorText(audience, core.getMessage("generic.noconsole"));
            return true;
        }
        utils.sendColorText(audience, "Max is " + permissionHandler.getNumberValue(args[0], user));
        return true;
    }
    
}
