package com.wolfco.main.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.wolfco.main.core;
import com.wolfco.main.classes.PlayerData;
import com.wolfco.common.utils;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;

public class teleportaccept implements CoreCommandExecutor {
    public Command getCommand() {
        return new Command("teleportaccept","wolfcore.tpaccept", new ArrayList<>());
    }

    @Override
    public core fetchCore() {
        return core;
    }
    
    core core;
    public teleportaccept(com.wolfco.main.core core) {
        this.core = core;
    }

    @Override
    public boolean execute(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            utils.sendColorText(core.adventure().sender(sender), core.getMessage("generic.noconsole"));
            return false;
        }
        Player receiver = (Player) sender;
        PlayerData receiverData = core.playerManager.getPlayerData(receiver);
        if (receiverData == null) {
            utils.sendColorText(core.adventure().sender(sender), core.getMessage("generic.invaliddata"));
            return false;
        }
        if (receiverData.lastRequest == null) {
            utils.sendColorText(core.adventure().sender(sender), core.getMessage("teleportask.norequest"));
            return false;
        }
        if (receiverData.lastRequest.type.equalsIgnoreCase("tpa")) {
            receiverData.lastRequest.host.teleport(receiver);
            utils.sendColorText(core.adventure().sender(receiverData.lastRequest.host), core.getMessage("teleportask.teleporting", List.of(receiver.getName())));
            utils.sendColorText(core.adventure().sender(receiver), core.getMessage("teleportask.accept", List.of(receiverData.lastRequest.host.getName())));
        } else if (receiverData.lastRequest.type.equalsIgnoreCase("tpahere")) {
            receiver.teleport(receiverData.lastRequest.host);
            utils.sendColorText(core.adventure().sender(receiverData.lastRequest.host), core.getMessage("teleportask.accept", List.of(receiver.getName())));
            utils.sendColorText(core.adventure().sender(receiver), core.getMessage("teleportask.teleporting", List.of(receiverData.lastRequest.host.getName())));
        }
        return true;
    }
}
