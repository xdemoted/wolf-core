package com.wolfco.main.commands;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.main.Core;
import com.wolfco.main.classes.PlayerData;
import com.wolfco.main.classes.customargs.HomeArgument;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class Home implements CoreCommand {
    @Override
    public Command getCommand() {
        Command command = new Command().setName("home");
        command.setAccessType(AccessType.PLAYER);
        command.addArguments(new HomeArgument(false));

        return command;
    }

    @Inject
    Core core;
    
    @Override
    public boolean onCommand(CommandSourceStack commandStack, String[] args, Object[] argumentValues) {
        CommandSender sender = commandStack.getSender();
        com.wolfco.main.classes.Home home = (com.wolfco.main.classes.Home) argumentValues[0];

        if (home == null) {
            PlayerData playerData = core.getPlayerManager().getPlayerData((Player) sender);
            if (playerData != null) {
                home = playerData.homes.get("home");
            } else {
                core.sendPreset(sender, "generic.invaliddata");
                return true;
            }
        }

        if (home == null) {
            core.sendPreset(sender, "home.notfound", List.of("home"));
            return true;
        }

        World world = core.getServer().getWorld(home.world);

        if (world == null) {
            core.sendPreset(sender, "home.worldinvalid", List.of(home.world.toString(), home.name));
            return true;
        }

        Location location = new Location(world, home.x, home.y, home.z, home.yaw, home.pitch);
        ((Player) sender).teleport(location);
        core.sendPreset(sender, "home.teleported", List.of(home.name));

        return true;
    }
}
