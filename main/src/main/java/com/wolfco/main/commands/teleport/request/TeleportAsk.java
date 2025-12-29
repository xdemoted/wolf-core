package com.wolfco.main.commands.teleport.request;

import java.util.Arrays;

import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.common.commands.arguments.PlayerArg;
import com.wolfco.main.Core;
import com.wolfco.main.classes.PlayerData;
import com.wolfco.main.classes.Request;
import com.wolfco.main.utility.FontUtil;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class TeleportAsk implements CoreCommand {
    static final String TPA = "tpa";
    static final String TPAHERE = TPA + "here";

    @Override
    public Command getCommand() {
        Command command = new Command().setName("teleportask");
        command.setAccessType(AccessType.PLAYER);
        command.addArguments(new PlayerArg(true).includeSender(false));
        command.addAliases("tpa");
        return command;
    }

    @Inject
    Core core;

    @Override
    public boolean onCommand(CommandSourceStack commandStack, String[] args,
            Object[] argumentValues) {
        return onCommandAlias(commandStack, TPA, args, argumentValues);
    }

    public boolean onCommandAlias(CommandSourceStack commandStack, String alias, String[] args,
            Object[] argumentValues) {
        Player sender = (Player) commandStack.getSender();
        Player receiver = (Player) argumentValues[0];
        PlayerData receiverData = core.getPlayerManager().getPlayerData(receiver);

        if (receiverData == null) {
            core.sendPreset(sender, "generic.playernotfound");
            return false;
        }

        Request existingRequest = receiverData.getRequest((Player) sender);

        if (existingRequest != null && existingRequest.type.equalsIgnoreCase(alias)
                && System.currentTimeMillis() - existingRequest.startTime < 30000) {
            core.sendPreset(sender, "teleportask.existing");
            return false;
        }

        String message = "";
        String requestType = "";

        switch (alias) {
            case "tpahere", "teleportaskhere" -> {
                requestType = TPAHERE;
                message = core.getPreset("teleportask.receivedhere", Arrays.asList(FontUtil.getPlayerTag(sender)));
                core.sendPreset(sender, "teleportask.receivedhere", Arrays.asList(FontUtil.getPlayerTag(sender)));
            }
            case "tpa", "teleportask" -> {
                requestType = TPA;
                message = core.getPreset("teleportask.received", Arrays.asList(FontUtil.getPlayerTag(sender)));
                core.sendPreset(sender, "teleportask.received", Arrays.asList(FontUtil.getPlayerTag(sender)));
            }
            default -> {
            }
        }

        receiverData.sendRequest((Player) sender, requestType);
        core.sendPreset(sender, "teleportask.sent", Arrays.asList(FontUtil.getPlayerTag(receiver)));
        core.sendMessage(receiver, message);
        return true;
    }
}
