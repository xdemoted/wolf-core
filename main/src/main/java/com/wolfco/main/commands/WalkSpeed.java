package com.wolfco.main.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.argumenthandlers.NumberArg;
import com.wolfco.common.classes.argumenthandlers.PlayerArg;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.main.Core;

public class WalkSpeed implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("walkspeed");
        command.setAccessType(AccessType.PLAYER);
        command.addArguments(
                new NumberArg(true, 1).setName("SPEED").setConstraints(0, 10),
                new PlayerArg(false).includeSender(false));

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public WalkSpeed(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args,
            Object[] argumentValues) {
        Double speed = (Double) argumentValues[0];
        Player target = (Player) argumentValues[1];

        if (!(target instanceof Player)) {
            target = (Player) sender;
        } else if (!sender.hasPermission("wolfcore.speed.others")) {
            core.sendPreset(sender, "generic.nopermission");
            return false;
        }

        float speedValue = speed.floatValue() * 0.1f; // 1 == 0.2, 10 == 1.0, 0 == 0.0
        
        if (speedValue > 1) {
            speedValue = 1;
        } else if (speedValue < 0) {
            speedValue = 0;
        }

        target.setWalkSpeed(speedValue);

        return true;
    }
}
