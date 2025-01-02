package com.wolfco.main.commands;

import java.util.List;

import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.main.Core;
import com.wolfco.main.classes.PlayerData;

public class TeleportAccept implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("teleportaccept");
        command.setDescription("Accept a teleport request");
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
        Player receiver = (Player) sender;
        PlayerData receiverData = core.getPlayerManager().getPlayerData(receiver);

        if (receiverData == null) {
            core.sendPreset(sender, "generic.invaliddata");
            return false;
        }

        if (receiverData.lastRequest == null) {
            core.sendPreset(sender, "teleportask.norequest");
            return false;
        }

        if (receiverData.lastRequest.type.equalsIgnoreCase("tpa")) {
            receiverData.lastRequest.host.teleport(receiver);
            core.sendPreset(sender, "teleportask.teleporting", List.of(receiver.getName()));
            core.sendPreset(sender, "teleportask.accept", List.of(receiverData.lastRequest.host.getName()));
        } else if (receiverData.lastRequest.type.equalsIgnoreCase("tpahere")) {
            receiver.teleport(receiverData.lastRequest.host);
            core.sendPreset(sender, "teleportask.accept", List.of(receiver.getName()));
            core.sendPreset(sender, "teleportask.teleporting", List.of(receiverData.lastRequest.host.getName()));
        }
        return true;
    }
}
