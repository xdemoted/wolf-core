package com.wolfco.main.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.Utilities;
import com.wolfco.common.classes.Argument;
import com.wolfco.common.classes.ArgumentInterface;
import com.wolfco.common.classes.ArgumentType;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.main.Core;
import com.wolfco.main.classes.Warp;
import com.wolfco.main.classes.customArgs.WarpArgument;

public class warp implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("warp");
        command.setDescription("Teleport to a warp");
        command.setNode("wolfcore.warp");

        List<ArgumentInterface> arguments = new ArrayList<>();
        arguments.add(new WarpArgument(true));
        arguments.add(new Argument(ArgumentType.PLAYER, false));
        command.setArguments(arguments);

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public warp(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        Warp warp;
        Player target;

        try {
            Object[] values = getCommand().getValues(core, sender, command, args);
            warp = (Warp) values[0];
            target = (Player) values[1];
        } catch (IllegalArgumentException e) {
            return false;
        }

        World world = core.getServer().getWorld(warp.world);

        if (world == null) {
            Utilities.sendColorText(core.getAdventure().sender(sender),
                    core.getMessage("warp.invalidworld", List.of(warp.world.toString(), warp.name)));

            return false;
        }

        if (sender instanceof Player && args.length == 1) {
            ((Player) sender).teleport(new Location(world, warp.x, warp.y, warp.z));
            Utilities.sendColorText(core.getAdventure().sender(sender), core.getMessage("warp.success", List.of(warp.name)));

            return true;
        } else if (target instanceof Player) {
            Utilities.sendColorText(core.getAdventure().sender(sender),
                    core.getMessage("warp.othersuccess", List.of(target.getName(), warp.name)));

            target.teleport(new Location(world, warp.x, warp.y, warp.z));

            return false;
        }
        return false;
    }

}