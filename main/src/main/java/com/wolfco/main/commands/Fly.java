package com.wolfco.main.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.argumenthandlers.BooleanArg;
import com.wolfco.common.classes.argumenthandlers.PlayerArg;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.main.Core;
import com.wolfco.main.utility.FontUtil;

public class Fly implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("fly");
        command.setAccessType(AccessType.PLAYER);
        command.addArguments(
                new PlayerArg(false).includeSender(false),
                new BooleanArg(false).setName("TOGGLE"));

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public Fly(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args,
            Object[] argumentValues) {
        Player target = (Player) argumentValues[0];

        if (!(target instanceof Player)) {
            target = (Player) sender;
        } else if (!sender.hasPermission("wolfcore.fly.others")) {
            core.sendPreset(sender, "generic.nopermission");
            return false;
        }
        
        boolean toggle;

        if (argumentValues[1] == null) {
            toggle = !target.getAllowFlight();
        } else {
            toggle = (boolean) argumentValues[1];
        }

        target.setAllowFlight(toggle);
        target.setFlying(toggle);

        if (sender != target) {
            core.sendPreset(sender, "fly.othersuccess", List.of(toggle ? "enabled" : "disabled", FontUtil.getPlayerTag(target)));
            core.sendPreset(target, "fly.success", List.of(toggle ? "enabled" : "disabled"));
        } else {
            core.sendPreset(sender, "fly.success", List.of(toggle ? "enabled" : "disabled"));
        }

        return true;
    }
}
