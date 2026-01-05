package com.wolfco.main.commands.teleport.warp;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.MessageUtility;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.common.commands.arguments.StringArg;
import com.wolfco.main.warps.Warp;
import com.wolfco.main.warps.WarpManager;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class SetWarp implements CoreCommand {
    @Override
    public Command getCommand() {
        Command command = new Command().setName("setwarp");
        command.setAccessType(AccessType.PLAYER);
        command.addArguments(new StringArg(true, true, false).setName("WARP"));

        return command;
    }

    @Inject
    MessageUtility messageUtility;

    @Inject
    WarpManager warpManager;

    @Override
    public boolean onCommand(CommandSourceStack commandStack, String[] args, Object[] argumentValues) {
        CommandSender sender = commandStack.getSender();
        String warpName = args[0];
        Player player = (Player) sender;
        Location location = player.getLocation();

        if (location == null) {
            messageUtility.sendPreset(sender, "generic.invaliddata");
            return true;
        }

        boolean result = warpManager.setWarp(
                Warp.fromLocation(location, warpName));

        if (!result)
            messageUtility.sendPreset(sender, "warp.setfail", List.of(warpName));

        messageUtility.sendPreset(sender, "warp.set", List.of(warpName));

        return true;
    }

}
