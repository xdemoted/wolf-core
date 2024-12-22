package com.wolfco.main.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.Utilities;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.main.Core;
import com.wolfco.main.classes.Home;
import com.wolfco.main.classes.PlayerData;
import com.wolfco.main.classes.customargs.HomeArgument;

import net.kyori.adventure.audience.Audience;

public class DelHome implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("delhome");
        command.setDescription("Used to delete homes");
        command.setNode("wolfcore.delhome");
        command.addArgument(new HomeArgument(true));

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public DelHome(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        Audience audience = core.getAdventure().sender(sender);
        Home home = (Home) argumentValues[0];

        if (!(sender instanceof Player)) {
            Utilities.sendColorText(audience, core.getMessage("generic.noconsole"));
            return true;
        }

        PlayerData playerData = core.getPlayerManager().getPlayerData((Player) sender);

        if (playerData != null) {
            Utilities.sendColorText(audience, core.getMessage("home.deleted", List.of(home.name)));
        }
        return true;
    }
}
