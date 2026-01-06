package com.wolfco.main.commands.teleport.request;

import java.util.List;

import org.bukkit.command.CommandSender;
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
public class TeleportAccept implements CoreCommand {
    @Override
    public Command getCommand() {
        Command command = new Command().setName("teleportaccept");
        command.addArguments(
            new PlayerArg(false).includeSender(true).setName("PLAYER")
        );
        command.setAccessType(AccessType.PLAYER);
        command.addAliases("tpaccept");
        return command;
    }



    @Inject
    ProfileManager profileManager;

    @Override
    public boolean onCommand(CommandSourceStack commandStack, String[] args, Object[] argumentValues) {
        CommandSender sender = commandStack.getSender();
        Player requestSender = (Player) argumentValues[0];
        Player receiver = (Player) sender;
        Profile receiverData = profileManager.getCachedProfile(receiver);

        if (receiverData == null) {
            MessageUtility.sendPreset(sender, "generic.invaliddata");
            return false;
        }

        Request targetRequest = receiverData.getRequest(requestSender);

        if (requestSender == null) {
            requestSender = receiverData.getRequestSender(targetRequest);

            if (requestSender == null) {
                MessageUtility.sendPreset(sender, "teleportask.norequest");
                return false;
            }
        }

        if (targetRequest == null) {
            MessageUtility.sendPreset(sender, "teleportask.norequest");
            return false;
        }

        if (targetRequest.type.equalsIgnoreCase("tpa")) {
            requestSender.teleport(receiver);
            MessageUtility.sendPreset(sender, "teleportask.teleporting", List.of(FontUtil.getPlayerTag(receiver)));
            MessageUtility.sendPreset(sender, "teleportask.accept", List.of(FontUtil.getPlayerTag(requestSender)));
        } else if (targetRequest.type.equalsIgnoreCase("tpahere")) {
            receiver.teleport(requestSender);
            MessageUtility.sendPreset(sender, "teleportask.accept", List.of(FontUtil.getPlayerTag(receiver)));
            MessageUtility.sendPreset(sender, "teleportask.teleporting", List.of(FontUtil.getPlayerTag(requestSender)));
        }
        receiverData.pendingRequests.remove(requestSender);
        return true;
    }
}
