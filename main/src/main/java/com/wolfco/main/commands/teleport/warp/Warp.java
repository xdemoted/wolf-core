package com.wolfco.main.commands.teleport.warp;

import java.util.Collection;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.commands.arguments.MultiPlayerArg;
import com.wolfco.main.Core;
import com.wolfco.main.commands.arguments.WarpArgument;
import com.wolfco.main.utility.FontUtil;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class Warp implements CoreCommand {
    @Override
    public Command getCommand() {
        Command command = new Command().setName("warp");
        
        command.addArguments(
            new WarpArgument(true),
            new MultiPlayerArg(false)
        );

        return command;
    }

    @Inject
    Core core;
    
    @Override
    public boolean onCommand(CommandSourceStack commandStack, String[] args, Object[] argumentValues) {
        CommandSender sender = commandStack.getSender();
        com.wolfco.main.warps.Warp warp = (com.wolfco.main.warps.Warp) argumentValues[0];
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
                core.sendPreset(sender, "warp.success.other", List.of(FontUtil.getPlayerTag(target.iterator().next()), warp.name));
            }

            for (Player player : target) {
                player.teleport(new Location(world, warp.x, warp.y, warp.z));
            }

            return true;
        }
        return false;
    }

}
