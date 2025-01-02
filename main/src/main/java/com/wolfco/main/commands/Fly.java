package com.wolfco.main.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Argument;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.common.classes.types.ArgumentType;
import com.wolfco.main.Core;

public class Fly implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("fly");
        command.setDescription("Set your fly toggle");
        command.setAccessType(AccessType.PLAYER);
        command.addArgument(new Argument(ArgumentType.EXCLUSIVEOTHERPLAYER, false));
        command.addArgument(new Argument(ArgumentType.BOOLEAN, false).setName("TOGGLE"));

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
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        boolean toggle = (boolean) argumentValues[1];
        Player target = (Player) argumentValues[0];

        if (!(target instanceof Player)) {
            target = (Player) sender;
        } else if (!sender.hasPermission("wolfcore.fly.others")) {
            core.sendPreset(sender, "generic.nopermission");
            return false;
        }

        if (argumentValues[1] == null) {
            toggle = !target.getAllowFlight();
        }

        target.setAllowFlight(toggle);
        target.setFlying(toggle);

        if (sender != target) {
            core.sendPreset(sender, "fly.othersuccess", List.of(toggle ? "enabled" : "disabled", target.getName()));
            core.sendPreset(target, "fly.success", List.of(toggle ? "enabled" : "disabled"));
        } else {
            core.sendPreset(sender, "fly.success", List.of(toggle ? "enabled" : "disabled"));
        }

        return true;
    }
}
