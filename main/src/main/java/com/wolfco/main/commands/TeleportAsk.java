package com.wolfco.main.commands;

import java.util.HashMap;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.Utilities;
import com.wolfco.common.classes.Argument;
import com.wolfco.common.classes.ArgumentType;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CommandTypes;
import com.wolfco.common.classes.CoreCommandExecutor;
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
        command.setDescription("Request a teleport to a player");
        command.setAccessType(CommandTypes.PLAYER);
        command.setNode("wolfcore.teleportask");
        command.addArgument(new Argument(ArgumentType.EXCLUSIVEOTHERPLAYER, true));

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
            Utilities.sendColorText(core.getAdventure().sender(sender), core.getMessage("generic.playernotfound"));
            return false;
        }

        if (receiverData.lastRequest.host == sender
                && System.currentTimeMillis() - receiverData.lastRequest.startTime < 30000) {
            Utilities.sendColorText(core.getAdventure().sender(sender), core.getMessage("teleportask.existing"));
            return false;
        } else {
            receiverData.sendRequest((Player) sender, requestTypes.get(command.getName()));
            Utilities.sendColorText(core.getAdventure().sender(sender),
                    core.getMessage("teleportask.sent", List.of(receiver.getName())));
            if (receiverData.lastRequest.type.equalsIgnoreCase("tpa")) {
                Utilities.sendColorText(core.getAdventure().sender(receiver),
                        core.getMessage("teleportask.received", List.of(sender.getName())));
            } else if (receiverData.lastRequest.type.equalsIgnoreCase("tpahere")) {
                Utilities.sendColorText(core.getAdventure().sender(receiver),
                        core.getMessage("teleportask.receivedhere", List.of(sender.getName())));
            }
            return true;
        }
    }
}
