package com.wolfco.main.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.main.Core;
import com.wolfco.main.classes.PlayerData;

public class TeleportDeny implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("teleportdeny");
        command.setDescription("Deny a teleport request");
        command.setAccessType(AccessType.PLAYER);

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public TeleportDeny(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        PlayerData playerData = core.getPlayerManager().getPlayerData((Player) sender);

        if (playerData == null) {
            core.sendPreset(sender, "generic.invaliddata");
            return false;
        }

        if (playerData.lastRequest == null) {
            core.sendPreset(sender, "teleportask.norequest");
            return false;
        }
        core.sendPreset(sender, "teleportask.deny", List.of(playerData.lastRequest.host.getName()));
        core.sendPreset(sender, "teleportask.deny", List.of(sender.getName()));
        playerData.lastRequest = null;

        return true;
    }

}
