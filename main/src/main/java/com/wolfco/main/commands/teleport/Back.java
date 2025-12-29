package com.wolfco.main.commands.teleport;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.main.Core;
import com.wolfco.main.classes.PlayerData;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class Back implements CoreCommand {

    @Override
    public Command getCommand() {
        Command command = new Command().setName("back");
        command.setAccessType(AccessType.PLAYER);

        return command;
    }


    @Inject
    Core core;

    @Override
    public boolean onCommand(CommandSourceStack commandStack, String[] args, Object[] argumentValues) {
        CommandSender sender = commandStack.getSender();
        PlayerData playerData = core.getPlayerManager().getPlayerData((Player) sender);

        if (playerData != null) {
            Location lastPosition = playerData.getLastLocation();

            if (lastPosition == null) {
                core.sendPreset(sender, "back.noposition");
                return false;
            }

            ((Player) sender).teleport(lastPosition);
            core.sendPreset(sender, "back.success");
        }
        return true;
    }
}
