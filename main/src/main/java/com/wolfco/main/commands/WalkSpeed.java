package com.wolfco.main.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Argument;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.common.classes.types.ArgumentType;
import com.wolfco.main.Core;

public class WalkSpeed implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("walkspeed");
        command.setDescription("Set your walk speed");
        command.setAccessType(AccessType.PLAYER);
        command.addArgument(new Argument(ArgumentType.INTEGER, true).setName("SPEED"));
        command.addArgument(new Argument(ArgumentType.EXCLUSIVEOTHERPLAYER, false));

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
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        int speed = (int) argumentValues[1];
        Player target = (Player) argumentValues[2];

        if (!(target instanceof Player)) {
            target = (Player) sender;
        } else if (!sender.hasPermission("wolfcore.speed.others")) {
            core.sendPreset(sender, "generic.nopermission");
            return false;
        }

        float speedValue = (speed > 1) ? 0.2f + (speed - 1) * 0.089f : speed * 0.2f; // 1 == 0.2, 10 == 1.0, 0 == 0.0

        target.setFlySpeed(speedValue);

        return true;
    }
}
