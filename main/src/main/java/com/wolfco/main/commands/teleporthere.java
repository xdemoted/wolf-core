package com.wolfco.main.commands;

import java.util.ArrayList;
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

public class teleporthere implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("teleporthere");
        command.setDescription("Teleport a player to you.");
        command.setNode("wolfcore.teleporthere");
        command.setAccessType(CommandTypes.PLAYER);
        command.setArguments(new ArrayList<>() {
            {
                add(new Argument(ArgumentType.OTHERPLAYER, true));
            }
        });
        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public teleporthere(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        Player player;

        try {
            player = (Player) getCommand().getValues(core, sender, command, args)[0];
        } catch (IllegalArgumentException e) {
            return false;
        }

        player.teleport(((Player) sender));
        Utilities.sendColorText(core.getAdventure().sender(sender), core.getMessage("teleporthere.success", List.of(player.getName())));
        return true;
    }
}