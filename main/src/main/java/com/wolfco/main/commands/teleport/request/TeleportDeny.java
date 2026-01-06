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

import io.papermc.paper.command.brigadier.CommandSourceStack;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class TeleportDeny implements CoreCommand {
    @Override
    public Command getCommand() {
        Command command = new Command().setName("teleportdeny");
        command.setAccessType(AccessType.PLAYER);
        command.addArguments(new PlayerArg(false).includeSender(false));
        command.addAliases("tpdeny");
        return command;
    }



    @Inject
    ProfileManager profileManager;

    @Override
    public boolean onCommand(CommandSourceStack commandStack, String[] args, Object[] argumentValues) {
        CommandSender sender = commandStack.getSender();
        Player target = (Player) argumentValues[0];
        Profile profile = profileManager.getCachedProfile((Player) sender);

        if (profile == null) {
            MessageUtility.sendPreset(sender, "generic.invaliddata");
            return false;
        }

        if (profile.pendingRequests.isEmpty()) {
            MessageUtility.sendPreset(sender, "teleportask.norequest");
            return false;
        }

        Request targetRequest = profile.getRequest(target);

        if (targetRequest == null) {
            MessageUtility.sendPreset(sender, "teleportask.norequest");
            return false;
        }

        MessageUtility.sendPreset(sender, "teleportask.deny", List.of(targetRequest.name));
        MessageUtility.sendPreset(sender, "teleportask.deny", List.of(sender.getName()));
        profile.denyRequest(target);

        return true;
    }

}
