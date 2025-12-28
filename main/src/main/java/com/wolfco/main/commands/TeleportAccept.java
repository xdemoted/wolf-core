package com.wolfco.main.commands;

import java.util.List;

import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.common.commands.arguments.PlayerArg;
import com.wolfco.main.Core;
import com.wolfco.main.classes.PlayerData;
import com.wolfco.main.classes.Request;
import com.wolfco.main.utility.FontUtil;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class TeleportAccept implements CoreCommand {
    @Override
    public Command getCommand() {
        Command command = new Command().setName("teleportaccept");
        command.addArguments(
            new PlayerArg(false).includeSender(true).setName("PLAYER")
        );
        command.setAccessType(AccessType.PLAYER);
        return command;
    }

    @Inject
    Core core;

    @Override
    public boolean execute(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        Player requestSender = (Player) argumentValues[0];
        Player receiver = (Player) sender;
        PlayerData receiverData = core.getPlayerManager().getPlayerData(receiver);

        if (receiverData == null) {
            core.sendPreset(sender, "generic.invaliddata");
            return false;
        }

        Request targetRequest = receiverData.getRequest(requestSender);

        if (requestSender == null) {
            requestSender = receiverData.getRequestSender(targetRequest);

            if (requestSender == null) {
                core.sendPreset(sender, "teleportask.norequest");
                return false;
            }
        }

        if (targetRequest == null) {
            core.sendPreset(sender, "teleportask.norequest");
            return false;
        }

        if (targetRequest.type.equalsIgnoreCase("tpa")) {
            requestSender.teleport(receiver);
            core.sendPreset(sender, "teleportask.teleporting", List.of(FontUtil.getPlayerTag(receiver)));
            core.sendPreset(sender, "teleportask.accept", List.of(FontUtil.getPlayerTag(requestSender)));
        } else if (targetRequest.type.equalsIgnoreCase("tpahere")) {
            receiver.teleport(requestSender);
            core.sendPreset(sender, "teleportask.accept", List.of(FontUtil.getPlayerTag(receiver)));
            core.sendPreset(sender, "teleportask.teleporting", List.of(FontUtil.getPlayerTag(requestSender)));
        }
        receiverData.pendingRequests.remove(requestSender);
        return true;
    }
}
