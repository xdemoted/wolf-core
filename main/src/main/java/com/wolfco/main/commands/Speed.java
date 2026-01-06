package com.wolfco.main.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.common.commands.arguments.NumberArg;
import com.wolfco.common.commands.arguments.PlayerArg;
import com.wolfco.common.commands.arguments.StaticArg;
import com.wolfco.common.MessageUtility;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import jakarta.inject.Singleton;

@Singleton
public class Speed implements CoreCommand {
    @Override
    public Command getCommand() {
        Command command = new Command().setName("speed");
        command.setNode("wolfcore.speed");
        command.setAccessType(AccessType.PLAYER);
        command.addArguments(
            new StaticArg(true, "walk", "fly").setName("MODE"),
            new NumberArg(true, 1).setConstraints(0, 10).setName("SPEED"),
            new PlayerArg(false).includeSender(false)
        );

        return command;
    }
    
    @Override
    public boolean onCommand(CommandSourceStack commandStack, String[] args, Object[] argumentValues) {
        CommandSender sender = commandStack.getSender();
        String mode = (String) argumentValues[0];
        Double speed = (Double) argumentValues[1];
        Player target = (Player) argumentValues[2];

        if (!(target instanceof Player)) {
            target = (Player) sender;
        } else if (!sender.hasPermission("wolfcore.speed.other")) {
            MessageUtility.sendPreset(sender, "generic.nopermission");
            return false;
        }

        float speedValue = speed.floatValue() * 0.1f; // 1 == 0.2, 10 == 1.0, 0 == 0.0
        
        if (speedValue > 1) {
            speedValue = 1;
        } else if (speedValue < 0) {
            speedValue = 0;
        }

        switch (mode) {
            case "fly" -> {
                target.setFlySpeed(speedValue);
                MessageUtility.sendPreset(sender, "speed.success", List.of(mode, Double.toString(speed)));
            }
            case "walk" -> {
                target.setWalkSpeed(speedValue);
                MessageUtility.sendPreset(sender, "speed.success", List.of(mode, Double.toString(speed)));
            }
            default -> {
                return false;
            }
        }

        return true;
    }
}
