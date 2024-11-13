package com.wolfco.main.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.main.core;
import com.wolfco.main.classes.Warp;
import com.wolfco.main.classes.customArgs.WarpArg;
import com.wolfco.common.utils;
import com.wolfco.common.classes.Argument;
import com.wolfco.common.classes.ArgumentType;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;

public class warp implements CoreCommandExecutor {
    @Override
    public Command getCommand() {
        Command command = new Command("warp");
        command.setDescription("Teleports you to a warp location.");
        command.setNode("wolfcore.warp");
        command.setOptions(new ArrayList<>() {
            {
                add(new WarpArg(true));
                add(new Argument(ArgumentType.PLAYER, false));
            }
        });

        return command;
    }

    @Override
    public core fetchCore() {
        return core;
    }
    
    core core;

    public warp(core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        Warp warp;

        try {
            warp = (Warp) getCommand().options.get(0).getValue(core, sender, command, args[0]);
        } catch (Exception e) {
            return false;
        }

        World world = core.getServer().getWorld(warp.world);
        if (world == null) {
            utils.sendColorText(core.getAdventure().sender(sender),
                    core.getMessage("warp.invalidworld", List.of(warp.world.toString(), warp.name)));
            return false;
        }

        if (sender instanceof Player && args.length == 1) {
            ((Player) sender).teleport(new Location(world, warp.x, warp.y, warp.z));
            utils.sendColorText(core.getAdventure().sender(sender), core.getMessage("warp.success", List.of(warp.name)));
            return true;
        } else if (args.length > 1) {
            Player player = (Player) getArgument(1).getValue(core, sender, command, args[1]);

            if (player == null) {
                utils.sendColorText(core.getAdventure().sender(sender), core.getMessage("generic.playernotfound"));
                return false;
            }
            
            utils.sendColorText(core.getAdventure().sender(sender),
                    core.getMessage("warp.othersuccess", List.of(player.getName(), warp.name)));
            player.teleport(new Location(world, warp.x, warp.y, warp.z));
            return false;
        }
        return false;
    }

}
