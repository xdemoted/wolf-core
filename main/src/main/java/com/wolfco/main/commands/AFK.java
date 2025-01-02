package com.wolfco.main.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.main.Core;
import com.wolfco.main.classes.Home;
import com.wolfco.main.classes.PlayerData;
import com.wolfco.main.classes.customargs.HomeArgument;

public class AFK implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("afk");
        command.setDescription("Become AFK.");
        command.setAccessType(AccessType.PLAYER);
        command.addArgument(new HomeArgument(true));

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public AFK(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        Home home = (Home) argumentValues[0];

        PlayerData playerData = core.getPlayerManager().getPlayerData((Player) sender);

        if (playerData != null) {
            core.sendPreset(sender, "home.deleted", List.of(home.name));
        }
        return true;
    }
}
