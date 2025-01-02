package com.wolfco.main.commands;

import java.io.IOException;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Argument;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.common.classes.types.ArgumentType;
import com.wolfco.main.Core;

import dev.dejvokep.boostedyaml.YamlDocument;

public class SetWarp implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("setwarp");
        command.setDescription("Set a warp");
        command.setAccessType(AccessType.PLAYER);
        command.addArgument(new Argument(ArgumentType.ALPHANUMERICSTRING, true).setName("WARPNAME"));

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public SetWarp(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        YamlDocument warps = core.getWarps();
        String warpName = args[0];
        Player player = (Player) sender;
        Location location = player.getLocation();

        if (warps.contains(warpName)) {
            core.sendPreset(sender, "warp.exists", List.of(warpName));
            return true;
        } else if (location == null) {
            core.sendPreset(sender, "generic.invaliddata");
            return true;
        }
        
        warps.set(warpName + ".x", location.getX());
        warps.set(warpName + ".y", location.getY());
        warps.set(warpName + ".z", location.getZ());
        warps.set(warpName + ".world", player.getWorld().getUID().toString());

        try {
            warps.save();
        } catch (IOException e) {
            core.sendMessage(sender, "<red>Failed to save warps file.");
        }

        core.sendPreset(sender, "warp.set", List.of(warpName));
        
        return true;
    }

}
