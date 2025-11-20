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
            if (playerData.lastWorld == null || playerData.lastPosition[0] == 0 && playerData.lastPosition[1] == 0 && playerData.lastPosition[2] == 0) {
                core.sendPreset(sender, "back.noposition");
                return false;
            }
            Location lastPos = new Location(
                core.getServer().getWorld(playerData.lastWorld),
                playerData.lastPosition[0],
                playerData.lastPosition[1],
                playerData.lastPosition[2],
                (float) playerData.lastPosition[3],
                (float) playerData.lastPosition[4]
            );
            ((Player) sender).teleport(lastPos);
            core.sendPreset(sender, "back.success");
        }
        return true;
    }
}
