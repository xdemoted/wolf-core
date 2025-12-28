package com.wolfco.main.commands;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.main.Core;
import com.wolfco.main.classes.PlayerData;
import com.wolfco.main.classes.customargs.OfflinePlayerArg;

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

    @Inject
    Core core;
    
    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String label, String[] args,
            Object[] argumentValues) {
        PlayerData player = (PlayerData) argumentValues[0];
        Location logoutLocation = player.getLogoutLocation();

        if (logoutLocation == null) {
            core.sendPreset(sender, "teleport.offline.nolocation", List.of(player.username));
            return false;
        }

        ((Player) sender).teleport(player.getLogoutLocation());
        core.sendPreset(sender, "teleport.success", List.of(player.username));
        return true;
    }
}
