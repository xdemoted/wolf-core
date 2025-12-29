package com.wolfco.main.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.common.commands.arguments.NumberArg;
import com.wolfco.common.commands.arguments.PlayerArg;
import com.wolfco.main.Core;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class WalkSpeed implements CoreCommand {
    @Override
    public Command getCommand() {
        Command command = new Command().setName("walkspeed");
        command.setAccessType(AccessType.PLAYER);
        command.addArguments(
                new NumberArg(true, 1).setName("SPEED").setConstraints(0, 10),
                new PlayerArg(false).includeSender(false));

        return command;
    }

    @Inject
    Core core;
    
    @Override
    public boolean onCommand(CommandSourceStack commandStack, String[] args,
            Object[] argumentValues) {
        CommandSender sender = commandStack.getSender();

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
