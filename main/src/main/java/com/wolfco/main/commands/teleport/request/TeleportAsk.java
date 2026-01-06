package com.wolfco.main.commands.teleport.request;

import java.util.Arrays;

import org.bukkit.entity.Player;

import com.wolfco.common.MessageUtility;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.common.commands.arguments.PlayerArg;
import com.wolfco.main.player.profiles.ProfileManager;
import com.wolfco.main.player.profiles.classes.Profile;
import com.wolfco.main.player.profiles.classes.Request;
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
    ProfileManager profileManager;

    @Override
    public boolean onCommand(CommandSourceStack commandStack, String[] args,
            Object[] argumentValues) {
        return onCommandAlias(commandStack, TPA, args, argumentValues);
    }

    public boolean onCommandAlias(CommandSourceStack commandStack, String alias, String[] args,
            Object[] argumentValues) {
        Player sender = (Player) commandStack.getSender();
        Player receiver = (Player) argumentValues[0];
        Profile receiverData = profileManager.getCachedProfile(receiver);

        if (receiverData == null) {
            MessageUtility.sendPreset(sender, "generic.playernotfound");
            return false;
        }

        Request existingRequest = receiverData.getRequest((Player) sender);

        if (existingRequest != null && existingRequest.type.equalsIgnoreCase(alias)
                && System.currentTimeMillis() - existingRequest.startTime < 30000) {
            MessageUtility.sendPreset(sender, "teleportask.existing");
            return false;
        }

        String message = "";
        String requestType = "";

        switch (alias) {
            case "tpahere", "teleportaskhere" -> {
                requestType = TPAHERE;
                message = MessageUtility.getPreset("teleportask.receivedhere", Arrays.asList(FontUtil.getPlayerTag(sender)));
                MessageUtility.sendPreset(sender, "teleportask.receivedhere", Arrays.asList(FontUtil.getPlayerTag(sender)));
            }
            case "tpa", "teleportask" -> {
                requestType = TPA;
                message = MessageUtility.getPreset("teleportask.received", Arrays.asList(FontUtil.getPlayerTag(sender)));
                MessageUtility.sendPreset(sender, "teleportask.received", Arrays.asList(FontUtil.getPlayerTag(sender)));
            }
            default -> {
            }
        }

        receiverData.sendRequest((Player) sender, requestType);
        MessageUtility.sendPreset(sender, "teleportask.sent", Arrays.asList(FontUtil.getPlayerTag(receiver)));
        MessageUtility.sendMessage(receiver, message);
        return true;
    }
}
