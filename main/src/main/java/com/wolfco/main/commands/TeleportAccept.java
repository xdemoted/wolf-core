package com.wolfco.main.commands;

import java.util.List;

import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.argumenthandlers.PlayerArg;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.main.Core;
import com.wolfco.main.classes.PlayerData;
import com.wolfco.main.classes.Request;

public class TeleportAccept implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("teleportaccept");
        command.addArguments(
            new PlayerArg(false).includeSender(true).setName("PLAYER")
        );
        command.setAccessType(AccessType.PLAYER);
        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public TeleportAccept(com.wolfco.main.Core core) {
        this.core = core;
    }

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
            core.sendPreset(sender, "teleportask.teleporting", List.of(receiver.getName()));
            core.sendPreset(sender, "teleportask.accept", List.of(requestSender.getName()));
        } else if (targetRequest.type.equalsIgnoreCase("tpahere")) {
            receiver.teleport(requestSender);
            core.sendPreset(sender, "teleportask.accept", List.of(receiver.getName()));
            core.sendPreset(sender, "teleportask.teleporting", List.of(requestSender.getName()));
        }
        receiverData.pendingRequests.remove(requestSender);
        return true;
    }
}
