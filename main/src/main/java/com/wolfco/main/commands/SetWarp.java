package com.wolfco.main.commands;

import java.io.IOException;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.Utilities;
import com.wolfco.common.classes.Argument;
import com.wolfco.common.classes.ArgumentType;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CommandTypes;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.main.Core;

import dev.dejvokep.boostedyaml.YamlDocument;
import net.kyori.adventure.audience.Audience;

public class SetWarp implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("setwarp");
        command.setDescription("Set a warp");
        command.setNode("wolfcore.setwarp");
        command.setAccessType(CommandTypes.PLAYER);
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
        Audience audience = core.getAdventure().sender(sender);
        YamlDocument warps = core.getWarps();
        String warpName = args[0];
        Player player = (Player) sender;
        Location location = player.getLocation();

        if (warps.contains(warpName)) {
            Utilities.sendColorText(audience, core.getMessage("warp.exists", List.of(warpName)));
            return true;
        } else if (location == null) {
            Utilities.sendColorText(audience, core.getMessage("generic.invaliddata"));
            return true;
        }
        
        warps.set(warpName + ".x", location.getX());
        warps.set(warpName + ".y", location.getY());
        warps.set(warpName + ".z", location.getZ());
        warps.set(warpName + ".world", player.getWorld().getUID().toString());

        try {
            warps.save();
        } catch (IOException e) {
            player.sendMessage("An error occurred while saving warps");
        }

        Utilities.sendColorText(audience, core.getMessage("warp.set", List.of(warpName)));
        
        return true;
    }

}
