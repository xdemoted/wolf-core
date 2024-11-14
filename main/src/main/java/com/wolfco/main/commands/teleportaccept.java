package com.wolfco.main.commands;

import java.util.List;

import org.bukkit.entity.Player;

import com.wolfco.common.Utilities;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CommandTypes;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.main.Core;
import com.wolfco.main.classes.PlayerData;

import net.kyori.adventure.audience.Audience;

public class teleportaccept implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("teleportaccept");
        command.setDescription("Accept a teleport request");
        command.setNode("wolfcore.teleportaccept");
        command.setAccessType(CommandTypes.PLAYER);
        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public teleportaccept(com.wolfco.main.Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        Audience audience = core.getAdventure().sender(sender);
        Player receiver = (Player) sender;
        PlayerData receiverData = core.PlayerManager.getPlayerData(receiver);

        if (receiverData == null) {
            Utilities.sendColorText(audience, core.getMessage("generic.invaliddata"));
            return false;
        }

        if (receiverData.lastRequest == null) {
            Utilities.sendColorText(audience, core.getMessage("teleportask.norequest"));
            return false;
        }

        if (receiverData.lastRequest.type.equalsIgnoreCase("tpa")) {
            receiverData.lastRequest.host.teleport(receiver);
            Utilities.sendColorText(core.getAdventure().sender(receiverData.lastRequest.host), core.getMessage("teleportask.teleporting", List.of(receiver.getName())));
            Utilities.sendColorText(core.getAdventure().sender(receiver), core.getMessage("teleportask.accept", List.of(receiverData.lastRequest.host.getName())));
        } else if (receiverData.lastRequest.type.equalsIgnoreCase("tpahere")) {
            receiver.teleport(receiverData.lastRequest.host);
            Utilities.sendColorText(core.getAdventure().sender(receiverData.lastRequest.host), core.getMessage("teleportask.accept", List.of(receiver.getName())));
            Utilities.sendColorText(core.getAdventure().sender(receiver), core.getMessage("teleportask.teleporting", List.of(receiverData.lastRequest.host.getName())));
        }
        return true;
    }
}
