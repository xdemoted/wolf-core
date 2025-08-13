package com.wolfco.main.commands;

import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.main.Core;

public class TeleportAccept implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("teleportaccept");
        command.setAccessType(AccessType.PLAYER);
        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public TeleportAccept(com.wolfco.main.Core core) {
        this.core = core;
    }

    // TODO: Finish teleport accept command

    @Override
    public boolean execute(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        Player receiver = (Player) sender;
        var receiverData = core.getPlayerManager().getPlayerData(receiver);

        return true;
    }
}
