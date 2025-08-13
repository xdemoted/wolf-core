package com.wolfco.main.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.main.Core;

public class TeleportDeny implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("teleportdeny");
        command.setAccessType(AccessType.PLAYER);

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public TeleportDeny(Core core) {
        this.core = core;
    }

    // TODO: Finish teleport deny command

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        var playerData = core.getPlayerManager().getPlayerData((Player) sender);

        

        return true;
    }

}
