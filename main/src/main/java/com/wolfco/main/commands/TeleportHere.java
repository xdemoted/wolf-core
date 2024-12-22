package com.wolfco.main.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.Utilities;
import com.wolfco.common.classes.Argument;
import com.wolfco.common.classes.ArgumentType;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CommandTypes;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.main.Core;

public class TeleportHere implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("teleporthere");
        command.setDescription("Teleport a player to you.");
        command.setNode("wolfcore.teleporthere");
        command.setAccessType(CommandTypes.PLAYER);
        command.addArgument(new Argument(ArgumentType.EXCLUSIVEOTHERPLAYER, true));
        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public TeleportHere(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        Player player = (Player) argumentValues[0];

        player.teleport(((Player) sender));
        Utilities.sendColorText(core.getAdventure().sender(sender), core.getMessage("teleporthere.success", List.of(player.getName())));
        return true;
    }
}
