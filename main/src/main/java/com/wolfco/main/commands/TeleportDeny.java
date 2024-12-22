package com.wolfco.main.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.Utilities;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CommandTypes;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.main.Core;
import com.wolfco.main.classes.PlayerData;

public class TeleportDeny implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("teleportdeny");
        command.setDescription("Deny a teleport request");
        command.setAccessType(CommandTypes.PLAYER);
        command.setNode("wolfcore.teleportdeny");

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
            Utilities.sendColorText(core.getAdventure().sender(sender), core.getMessage("generic.invaliddata"));
            return false;
        }

        if (playerData.lastRequest == null) {
            Utilities.sendColorText(core.getAdventure().sender(sender), core.getMessage("teleportask.norequest"));
            return false;
        }
        Utilities.sendColorText(core.getAdventure().sender(sender),
                core.getMessage("teleportask.deny", List.of(playerData.lastRequest.host.getName())));
        Utilities.sendColorText(core.getAdventure().sender(playerData.lastRequest.host),
                core.getMessage("teleportask.deny", List.of(sender.getName())));
        playerData.lastRequest = null;

        return true;
    }

}
