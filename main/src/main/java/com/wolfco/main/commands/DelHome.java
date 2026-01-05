package com.wolfco.main.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.main.Core;
import com.wolfco.main.commands.arguments.HomeArgument;
import com.wolfco.main.profiles.ProfileManager;
import com.wolfco.main.profiles.classes.Home;
import com.wolfco.main.profiles.classes.Profile;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class DelHome implements CoreCommand {

    @Override
    public Command getCommand() {
        Command command = new Command().setName("delhome");
        command.setAccessType(AccessType.PLAYER);
        command.addArguments(new HomeArgument(true));

        return command;
    }

    @Inject
    Core core;

    @Inject
    ProfileManager profileManager;

    @Override
    public boolean onCommand(CommandSourceStack commandStack, String[] args, Object[] argumentValues) {
        CommandSender sender = commandStack.getSender();
        Home home = (Home) argumentValues[0];

        Profile profile = profileManager.getCachedProfile((Player) sender);

        if (profile != null) {
            profile.homes.remove(home.name);
            core.sendPreset(sender, "home.deleted", List.of(home.name));
        }
        return true;
    }
}
