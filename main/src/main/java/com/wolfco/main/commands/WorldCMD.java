package com.wolfco.main.commands;

import java.util.Collection;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.argumenthandlers.MultiPlayerArg;
import com.wolfco.common.classes.argumenthandlers.WorldArg;
import com.wolfco.main.Core;

public class WorldCMD implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("world");
        command.addArguments(new WorldArg(true), new MultiPlayerArg(false));

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public WorldCMD(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias,
            String[] args, Object[] argumentValues) {
        World world = (World) argumentValues[0];

        @SuppressWarnings("unchecked")
        Collection<? extends Player> players = (Collection<? extends Player>) argumentValues[1];

        if (players == null) {
            ((Player) sender).teleport(world.getSpawnLocation());
        } else {
            for (Player player : players) {
                player.teleport(world.getSpawnLocation());
            }
        }

        core.sendMessage(sender, "<#ffaa00>Teleported %s to world %s");
        return true;
    }
}
