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
import com.wolfco.main.commands.arguments.HomeArgument;
import com.wolfco.main.player.profiles.ProfileManager;
import com.wolfco.main.player.profiles.classes.Profile;

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

    @Inject
    ProfileManager profileManager;

    @Override
    public boolean onCommand(CommandSourceStack commandStack, String[] args, Object[] argumentValues) {
        CommandSender sender = commandStack.getSender();
        com.wolfco.main.player.profiles.classes.Home home = (com.wolfco.main.player.profiles.classes.Home) argumentValues[0];

        if (home == null) {
            Profile profile = profileManager.getCachedProfile((Player) sender);
            if (profile != null) {
                home = profile.homes.get("home");
            } else {
                getMessageUtility().sendPreset(sender, "generic.invaliddata");
                return true;
            }
        }

        if (home == null) {
            getMessageUtility().sendPreset(sender, "home.notfound", List.of("home"));
            return true;
        }

        World world = core.getServer().getWorld(home.world);

        if (world == null) {
            getMessageUtility().sendPreset(sender, "home.worldinvalid", List.of(home.world.toString(), home.name));
            return true;
        }

        Location location = new Location(world, home.x, home.y, home.z, home.yaw, home.pitch);
        ((Player) sender).teleport(location);
        getMessageUtility().sendPreset(sender, "home.teleported", List.of(home.name));

        return true;
    }
}
