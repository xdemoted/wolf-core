package com.wolfco.main.commands;

import java.io.IOException;
import java.util.ArrayList;
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

import net.kyori.adventure.audience.Audience;

public class setwarp implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("setwarp");
        command.setDescription("Set a warp");
        command.setNode("wolfcore.setwarp");
        command.setAccessType(CommandTypes.PLAYER);
        command.setArguments(new ArrayList<>() {
            {
                add(new Argument(ArgumentType.ALPHANUMERICSTRING, true));
            }
        });

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public setwarp(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        Audience audience = core.getAdventure().sender(sender);
        String warpName = args[0];
        if (!(sender instanceof Player)) {
            Utilities.sendColorText(audience, core.getMessage("generic.noconsole"));
            return false;
        }
        Player player = (Player) sender;
        Location location = player.getLocation();

        if (core.warps.contains(warpName)) {
            Utilities.sendColorText(audience, core.getMessage("warp.exists", List.of(warpName)));
            return true;
        } else if (location == null) {
            Utilities.sendColorText(audience, core.getMessage("generic.invaliddata"));
            return true;
        }
        
        core.warps.set(warpName + ".x", location.getX());
        core.warps.set(warpName + ".y", location.getY());
        core.warps.set(warpName + ".z", location.getZ());
        core.warps.set(warpName + ".world", player.getWorld().getUID().toString());

        try {
            core.warps.save();
        } catch (IOException e) {

        }

        Utilities.sendColorText(audience, core.getMessage("warp.set", List.of(warpName)));
        return true;
    }

}
