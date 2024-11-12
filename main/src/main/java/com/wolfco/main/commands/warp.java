package com.wolfco.main.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.main.Core;
import com.wolfco.main.classes.Warp;
import com.wolfco.main.classes.customArgs.WarpArgument;
import com.wolfco.common.Utilities;
import com.wolfco.common.classes.Argument;
import com.wolfco.common.classes.ArgumentType;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;

public class warp implements CoreCommandExecutor {
    @Override
    public Command getCommand() {
        Command command = new Command("warp");
        command.setDescription("Teleport to a warp");
        command.setNode("wolfcore.warp");

        List<Argument> arguments = new ArrayList<>();
        arguments.add(new WarpArgument(true));
        arguments.add(new Argument(ArgumentType.PLAYER, false));
        command.setArguments(arguments);
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
        Warp warp = getCommand().options.get(0).getWarp(core.warps, args[0]);
        if (warp == null) {
            Utilities.sendColorText(core.getAdventure().sender(sender), core.getMessage("warp.notfound", List.of(args[0])));
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
        } else if (args.length > 1) {
            Player player = getCommand().options.get(1).getExclusivePlayer(core, args[1]);
            if (player == null) {
                Utilities.sendColorText(core.getAdventure().sender(sender), core.getMessage("generic.playernotfound"));
                return false;
            }
            Utilities.sendColorText(core.getAdventure().sender(sender),
                    core.getMessage("warp.othersuccess", List.of(player.getName(), warp.name)));
            player.teleport(new Location(world, warp.x, warp.y, warp.z));
            return false;
        }
        return false;
    }

}
