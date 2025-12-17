package com.wolfco.main.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.main.Core;
import com.wolfco.main.classes.PlayerData;

public class Back implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("back");
        command.setAccessType(AccessType.PLAYER);

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public Back(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        PlayerData playerData = core.getPlayerManager().getPlayerData((Player) sender);

        if (playerData != null) {
            Location lastPosition = playerData.getLastLocation();

            if (lastPosition == null) {
                core.sendPreset(sender, "back.noposition");
                return false;
            }

            ((Player) sender).teleport(lastPosition);
            core.sendPreset(sender, "back.success");
        }
        return true;
    }
}
