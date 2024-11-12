package com.wolfco.main.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.main.Core;
import com.wolfco.main.classes.Warp;
<<<<<<< HEAD
import com.wolfco.main.classes.customArgs.WarpArgument;
import com.wolfco.common.Utilities;
=======
import com.wolfco.main.classes.customArgs.WarpArg;
import com.wolfco.common.utils;
>>>>>>> 176256cb29a2aceab31316a77fe99b1426fa0668
import com.wolfco.common.classes.Argument;
import com.wolfco.common.classes.ArgumentType;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;

public class warp implements CoreCommandExecutor {
    @Override
    public Command getCommand() {
        Command command = new Command("warp");
<<<<<<< HEAD
        command.setDescription("Teleport to a warp");
        command.setNode("wolfcore.warp");

        List<Argument> arguments = new ArrayList<>();
        arguments.add(new WarpArgument(true));
        arguments.add(new Argument(ArgumentType.PLAYER, false));
        command.setArguments(arguments);
=======
        command.setDescription("Teleports you to a warp location.");
        command.setNode("wolfcore.warp");
        command.setOptions(new ArrayList<>() {
            {
                add(new WarpArg(true));
                add(new Argument(ArgumentType.PLAYER, false));
            }
        });

        return command;
>>>>>>> 176256cb29a2aceab31316a77fe99b1426fa0668
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
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
<<<<<<< HEAD
        Warp warp = getCommand().options.get(0).getWarp(core.warps, args[0]);
        if (warp == null) {
            Utilities.sendColorText(core.getAdventure().sender(sender), core.getMessage("warp.notfound", List.of(args[0])));
=======
        Warp warp;

        try {
            warp = (Warp) getCommand().options.get(0).getValue(core, sender, command, args[0]);
        } catch (Exception e) {
>>>>>>> 176256cb29a2aceab31316a77fe99b1426fa0668
            return false;
        }

        World world = core.getServer().getWorld(warp.world);
        if (world == null) {
<<<<<<< HEAD
            Utilities.sendColorText(core.getAdventure().sender(sender),
=======
            utils.sendColorText(core.getAdventure().sender(sender),
>>>>>>> 176256cb29a2aceab31316a77fe99b1426fa0668
                    core.getMessage("warp.invalidworld", List.of(warp.world.toString(), warp.name)));
            return false;
        }

        if (sender instanceof Player && args.length == 1) {
            ((Player) sender).teleport(new Location(world, warp.x, warp.y, warp.z));
<<<<<<< HEAD
            Utilities.sendColorText(core.getAdventure().sender(sender), core.getMessage("warp.success", List.of(warp.name)));
=======
            utils.sendColorText(core.getAdventure().sender(sender), core.getMessage("warp.success", List.of(warp.name)));
>>>>>>> 176256cb29a2aceab31316a77fe99b1426fa0668
            return true;
        } else if (args.length > 1) {
            Player player = (Player) getArgument(1).getValue(core, sender, command, args[1]);

            if (player == null) {
<<<<<<< HEAD
                Utilities.sendColorText(core.getAdventure().sender(sender), core.getMessage("generic.playernotfound"));
                return false;
            }
            Utilities.sendColorText(core.getAdventure().sender(sender),
=======
                utils.sendColorText(core.getAdventure().sender(sender), core.getMessage("generic.playernotfound"));
                return false;
            }
            
            utils.sendColorText(core.getAdventure().sender(sender),
>>>>>>> 176256cb29a2aceab31316a77fe99b1426fa0668
                    core.getMessage("warp.othersuccess", List.of(player.getName(), warp.name)));
            player.teleport(new Location(world, warp.x, warp.y, warp.z));
            return false;
        }
        return false;
    }

}
