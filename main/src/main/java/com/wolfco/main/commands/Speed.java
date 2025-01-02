package com.wolfco.main.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Argument;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.argumenthandlers.StaticArg;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.common.classes.types.ArgumentType;
import com.wolfco.main.Core;


public class Speed implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("speed");
        command.setDescription("Set your movement speed");
        command.setAccessType(AccessType.PLAYER);
        command.addArgument(new StaticArg(true, List.of("walk", "fly")));
        command.addArgument(new Argument(ArgumentType.DOUBLE, true).setName("SPEED"));
        command.addArgument(new Argument(ArgumentType.EXCLUSIVEOTHERPLAYER, false));

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public Speed(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        String mode = (String) argumentValues[0];
        double speed = (double) argumentValues[1];
        Player target = (Player) argumentValues[2];

        if (!(target instanceof Player)) {
            target = (Player) sender;
        } else if (!sender.hasPermission("wolfcore.speed.other")) {
            core.sendPreset(sender, "generic.nopermission");
            return false;
        }

        float speedValue = (float) speed * 0.1f; // 1 == 0.2, 10 == 1.0, 0 == 0.0
        
        if (speedValue > 1) {
            speedValue = 1;
        } else if (speedValue < 0) {
            speedValue = 0;
        }

        switch (mode) {
            case "fly" -> {
                target.setFlySpeed(speedValue);
                core.sendPreset(sender, "speed.success", List.of(mode, Double.toString(speed)));
            }
            case "walk" -> target.setWalkSpeed(speedValue);
            default -> {
                return false;
            }
        }

        return true;
    }
}
