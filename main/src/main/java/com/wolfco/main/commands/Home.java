package com.wolfco.main.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.Utilities;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CommandTypes;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.main.Core;
import com.wolfco.main.classes.PlayerData;
import com.wolfco.main.classes.customArgs.HomeArgument;

import net.kyori.adventure.audience.Audience;

public class Home implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("home");
        command.setDescription("Used to teleport to a home");
        command.setNode("wolfcore.home");
        command.setAccessType(CommandTypes.PLAYER);
        command.setArguments(new ArrayList<>() {
            {
                add(new HomeArgument(false));
            }
        });

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public Home(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        Audience audience = core.getAdventure().sender(sender);
        com.wolfco.main.classes.Home home = (com.wolfco.main.classes.Home) argumentValues[0];

        if (home == null) {
            PlayerData playerData = core.PlayerManager.getPlayerData((Player) sender);
            if (playerData != null) {
                home = playerData.homes.get("home");
            } else {
                Utilities.sendColorText(audience, core.getMessage("generic.invaliddata"));
                return true;
            }
        }

        if (home == null) {
            Utilities.sendColorText(audience, core.getMessage("home.notfound", List.of("home")));
            return true;
        }

        World world = core.getServer().getWorld(home.world);

        if (world == null) {
            Utilities.sendColorText(audience, core.getMessage("home.worldinvalid", List.of(home.world.toString(), home.name)));
            return true;
        }

        Location location = new Location(world, home.x, home.y, home.z, home.yaw, home.pitch);
        ((Player) sender).teleport(location);
        Utilities.sendColorText(audience, core.getMessage("home.teleported", List.of(home.name)));

        return true;
    }
}
