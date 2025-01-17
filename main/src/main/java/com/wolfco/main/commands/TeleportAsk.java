package com.wolfco.main.commands;

import java.util.HashMap;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.argumenthandlers.PlayerArg;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.main.Core;
import com.wolfco.main.classes.PlayerData;

public class TeleportAsk implements CoreCommandExecutor {

    HashMap<String, String> requestTypes = new HashMap<>();
    static final String TPA = "tpa";
    static final String TPAHERE = TPA + "here";

    public TeleportAsk() {
        requestTypes.put("teleportask", TPA);
        requestTypes.put(TPA, TPA);
        requestTypes.put(TPAHERE, TPAHERE);
        requestTypes.put("teleportaskhere", TPAHERE);
    }

    @Override
    public Command getCommand() {
        Command command = new Command("teleportask");
        command.setAccessType(AccessType.PLAYER);
        command.addArguments(new PlayerArg(true).includeSender(false));

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public TeleportAsk(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        Player receiver = (Player) argumentValues[0];
        PlayerData receiverData = core.getPlayerManager().getPlayerData(receiver);

        if (receiverData == null) {
            core.sendPreset(sender, "generic.playernotfound");
            return false;
        }

        if (receiverData.lastRequest.host == sender
                && System.currentTimeMillis() - receiverData.lastRequest.startTime < 30000) {
            core.sendPreset(sender, "teleportask.existing");
            return false;
        } else {
            receiverData.sendRequest((Player) sender, requestTypes.get(command.getName()));
            core.sendPreset(sender, "teleportask.sent", List.of(receiver.getName()));
            if (receiverData.lastRequest.type.equalsIgnoreCase("tpa")) {
                core.sendPreset(sender, "teleportask.received", List.of(sender.getName()));
            } else if (receiverData.lastRequest.type.equalsIgnoreCase("tpahere")) {
                core.sendPreset(sender, "teleportask.receivedhere", List.of(sender.getName()));
            }
            return true;
        }
    }
}
