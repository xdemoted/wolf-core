package com.wolfco.main.commands.teleport.warp;

import java.io.IOException;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.MessageUtility;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.common.commands.arguments.StringArg;
import com.wolfco.main.Core;

import dev.dejvokep.boostedyaml.YamlDocument;
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
    Core core;
    
    @Override
    public boolean onCommand(CommandSourceStack commandStack, String[] args, Object[] argumentValues) {
        CommandSender sender = commandStack.getSender();
        YamlDocument warps = core.getWarps(); // #TODO refactor to warp manager
        String warpName = args[0];
        Player player = (Player) sender;
        Location location = player.getLocation();

        if (warps.contains(warpName)) {
            messageUtility.sendPreset(sender, "warp.exists", List.of(warpName));
            return true;
        } else if (location == null) {
            messageUtility.sendPreset(sender, "generic.invaliddata");
            return true;
        }
        
        warps.set(warpName + ".x", location.getX());
        warps.set(warpName + ".y", location.getY());
        warps.set(warpName + ".z", location.getZ());
        warps.set(warpName + ".world", player.getWorld().getUID().toString());

        try {
            warps.save();
        } catch (IOException e) {
            messageUtility.sendMessage(sender, "<red>Failed to save warps file.");
        }

        messageUtility.sendPreset(sender, "warp.set", List.of(warpName));
        
        return true;
    }

}
