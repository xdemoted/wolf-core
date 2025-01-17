package com.wolfco.main.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.argumenthandlers.NumberArg;
import com.wolfco.common.classes.argumenthandlers.PlayerArg;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.main.Core;

public class FlySpeed implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("flyspeed");
        command.setAccessType(AccessType.PLAYER);
        command.addArguments(
                new NumberArg(true, 1).setConstraints(0, 10).setName("SPEED"),
                new PlayerArg(false).includeSender(false));

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public FlySpeed(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args,
            Object[] argumentValues) {
        Double speed = (Double) argumentValues[1];
        Player target = (Player) argumentValues[2];

        if (!(target instanceof Player)) {
            target = (Player) sender;
        } else if (!sender.hasPermission("wolfcore.speed.others")) {
            core.sendPreset(sender, "generic.nopermission");
            return false;
        }

        double speedValue = (speed > 1) ? 0.2 + (speed - 1) * 0.089 : speed * 0.2; // 1 == 0.2, 10 == 1.0, 0 == 0.0

        target.setFlySpeed((float) speedValue);

        return true;
    }
}
