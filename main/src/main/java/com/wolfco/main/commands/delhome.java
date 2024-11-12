package com.wolfco.main.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.kyori.adventure.audience.Audience;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.main.Core;
import com.wolfco.main.classes.Home;
import com.wolfco.main.classes.PlayerData;
import com.wolfco.main.classes.customArgs.HomeArgument;
import com.wolfco.common.Utilities;

import java.util.List;

public class delhome implements CoreCommandExecutor {
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

    public delhome(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        Audience audience = core.getAdventure().sender(sender);
        Home home;
        try {
            home = (Home) getArgument(0).getValue(core, sender, command, args[0]);
        } catch (Exception e) {
            return false;
        }

        if (!(sender instanceof Player)) {
            Utilities.sendColorText(audience, core.getMessage("generic.noconsole"));
            return true;
        }

        PlayerData playerData = core.PlayerManager.getPlayerData((Player) sender);

        if (playerData != null) {
            Utilities.sendColorText(audience, core.getMessage("home.deleted", List.of(home.name)));
        }
        return true;
    }
}
