package com.wolfco.main.commands.teleport;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.common.commands.arguments.PlayerArg;
import com.wolfco.main.Core;
import com.wolfco.main.utility.FontUtil;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class TeleportHere implements CoreCommand {
    @Override
    public Command getCommand() {
        Command command = new Command().setName("teleporthere");
        command.setAccessType(AccessType.PLAYER);
        command.addArguments(new PlayerArg(true).includeSender(false));
        command.addAliases("tphere");
        return command;
    }

    @Inject
    Core core;
    
    @Override
    public boolean onCommand(CommandSourceStack commandStack, String[] args, Object[] argumentValues) {
        CommandSender sender = commandStack.getSender();
        Player player = (Player) argumentValues[0];

        player.teleport(((Player) sender));
        core.sendPreset(sender, "teleporthere.success", List.of(FontUtil.getPlayerTag(player)));
        return true;
    }
}
