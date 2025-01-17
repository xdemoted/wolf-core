package com.wolfco.main.commands;

import java.util.Collection;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.argumenthandlers.MultiPlayerArg;
import com.wolfco.main.Core;
import com.wolfco.main.classes.customargs.WarpArgument;

public class Warp implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("warp");
        
        command.addArguments(
            new WarpArgument(true),
            new MultiPlayerArg(false)
        );

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public Warp(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        com.wolfco.main.classes.Warp warp = (com.wolfco.main.classes.Warp) argumentValues[0];
        @SuppressWarnings("unchecked")
        Collection<Player> target = (Collection<Player>) argumentValues[1];

        World world = core.getServer().getWorld(warp.world);

        if (world == null) {
            core.sendPreset(sender, "warp.invalidworld", List.of(warp.world.toString(), warp.name));

            return false;
        }

        if (sender instanceof Player && args.length == 1) {
            ((Player) sender).teleport(new Location(world, warp.x, warp.y, warp.z));
            core.sendPreset(sender, "warp.success.self", List.of(warp.name));

            return true;
        } else if (target != null) {
            if (target.size() > 1) {
                core.sendPreset(sender, "warp.success.all");
                return false;
            } else {
                core.sendPreset(sender, "warp.success.other", List.of(target.iterator().next().getName(), warp.name));
            }

            for (Player player : target) {
                player.teleport(new Location(world, warp.x, warp.y, warp.z));
            }

            return true;
        }
        return false;
    }

}
