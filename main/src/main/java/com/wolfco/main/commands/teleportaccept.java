package com.wolfco.main.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.wolfco.main.Core;
import com.wolfco.main.classes.PlayerData;
import com.wolfco.common.Utilities;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;

public class teleportaccept implements CoreCommandExecutor {
    public Command getCommand() {
        return new Command("teleportaccept","wolfcore.tpaccept", new ArrayList<>());
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
    public boolean execute(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            Utilities.sendColorText(core.getAdventure().sender(sender), core.getMessage("generic.noconsole"));
            return false;
        }
        Player receiver = (Player) sender;
        PlayerData receiverData = core.playerManager.getPlayerData(receiver);
        if (receiverData == null) {
            Utilities.sendColorText(core.getAdventure().sender(sender), core.getMessage("generic.invaliddata"));
            return false;
        }
        if (receiverData.lastRequest == null) {
            Utilities.sendColorText(core.getAdventure().sender(sender), core.getMessage("teleportask.norequest"));
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
