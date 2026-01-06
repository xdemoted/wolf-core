package com.wolfco.main.commands.teleport;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.MessageUtility;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.main.commands.arguments.OfflinePlayerArg;
import com.wolfco.main.player.profiles.classes.Profile;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class OfflineTeleport implements CoreCommand {
    @Override
    public Command getCommand() {
        Command command = new Command().setName("offlineteleport");
        command.addArguments(
                new OfflinePlayerArg(true).includeSender(false).setName("PLAYER"));

        return command;
    }


    
    @Override
    public boolean onCommand(CommandSourceStack commandStack, String[] args, Object[] argumentValues) {
        CommandSender sender = commandStack.getSender();

        Profile player = (Profile) argumentValues[0];
        Location logoutLocation = player.getLogoutLocation();

        if (logoutLocation == null) {
            MessageUtility.sendPreset(sender, "teleport.offline.nolocation", List.of(player.username));
            return false;
        }

        ((Player) sender).teleport(player.getLogoutLocation());
        MessageUtility.sendPreset(sender, "teleport.success", List.of(player.username));
        return true;
    }
}
