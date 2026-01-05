package com.wolfco.main.commands.teleport;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.common.commands.arguments.StringArg;
import com.wolfco.main.Core;
import com.wolfco.main.classes.Home;
import com.wolfco.main.handlers.PermissionHandler;
import com.wolfco.main.profiles.ProfileManager;
import com.wolfco.main.profiles.classes.Profile;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import net.luckperms.api.model.user.User;

@Singleton
public class SetHome implements CoreCommand {    static final String NODE = "wolfcore.sethome";
    private final Core core;
    private final PermissionHandler permissionHandler;
    private final ProfileManager profileManager;

    @Inject
    public SetHome(Core core, PermissionHandler permissionHandler, ProfileManager profileManager) {
        this.core = core;
        this.permissionHandler = permissionHandler;
        this.profileManager = profileManager;
    }

    @Override
    public Command getCommand() {
        Command command = new Command().setName("sethome");
        command.setAccessType(AccessType.PLAYER);
        command.addArguments(new StringArg(false, true, false).setName("HOME"));

        return command;
    }

    @Override
    public boolean onCommand(CommandSourceStack commandStack, String[] args, Object[] argumentValues) {
        CommandSender sender = commandStack.getSender();
        String home = (String) argumentValues[0];
        User user = core.getLuckPerms().getUserManager().getUser(sender.getName());

        if (home == null) {
            home = "home";
        }

        Profile profile = profileManager.getCachedProfile((Player) sender);

        if (profile != null) {
            int allowedHomes = permissionHandler.getNumberValue(NODE, user);

            if (profile.homes.size() >= allowedHomes && !profile.homes.containsKey(home)) {
                core.sendPreset(sender, "home.limit", List.of(Integer.toString(allowedHomes)));
                return true;
            }

            profile.homes.put(home, new Home(home, ((Player) sender).getLocation()));
            
            core.sendPreset(sender, "home.set", List.of(home));
        } else {
            core.sendPreset(sender, "generic.invaliddata");
        }
        return true;
    }

}
