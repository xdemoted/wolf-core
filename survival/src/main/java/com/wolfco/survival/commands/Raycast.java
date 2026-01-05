package com.wolfco.survival.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.common.commands.arguments.NumberArg;
import com.wolfco.survival.Core;
import com.wolfco.survival.explosions.BasicExplosion;
import com.wolfco.survival.geometryUtils.shapeGeneration.ExplosionSphere;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class Raycast implements CoreCommand {
    private final Core core;

    @Inject
    public Raycast(Core core) {
        this.core = core;
    }

    @Override
    public Command getCommand() {
        Command command = new Command().setName("raycast");
        command.setAccessType(AccessType.PLAYER);
        command.addArguments(
                new NumberArg(true, 0).setName("power"),
                new NumberArg(true, 0).setName("rayCount"));
        command.setAccessType(AccessType.ALL);
        return command;
    }

    @Override
    public boolean onCommand(CommandSourceStack commandStack, String[] args, Object[] argumentValues) {
        double power = (double) argumentValues[0];
        double rayCount = (double) argumentValues[1];
        CommandSender sender = commandStack.getSender();
        sender.sendMessage(sender.getClass().getName());

        if (sender instanceof Entity player) {
            core.log("Raycast Execute");
            Location loc = player.getLocation();

            BasicExplosion explosion = new BasicExplosion(loc, (int) rayCount, power);

            double maxDistance = explosion.getMaxDistance();

            ExplosionSphere sphere = new ExplosionSphere(loc, 0f, (float) maxDistance,
                    (int) Math.round(maxDistance * 1.2));

            sphere.startInterpolation();
            explosion.start(1);

            return true;
        }

        return false;
    }
}
