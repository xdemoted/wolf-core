package com.wolfco.main.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.main.Core;
import com.wolfco.main.classes.PlayerData;
import com.wolfco.common.Utilities;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;

public class teleportdeny implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        return new Command("teleportdeny","wolfcore.tpdeny", new ArrayList<>());
    }

    @Override
    public Core fetchCore() {
        return core;
    }
    
    Core core;

    public teleportdeny(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        if (sender instanceof Player) {
            PlayerData playerData = core.playerManager.getPlayerData((Player) sender);
            if (playerData == null) {
                Utilities.sendColorText(core.getAdventure().sender(sender), core.getMessage("generic.invaliddata"));
                return false;
            }
            if (playerData.lastRequest == null) {
                Utilities.sendColorText(core.getAdventure().sender(sender), core.getMessage("teleportask.norequest"));
                return false;
            }
            Utilities.sendColorText(core.getAdventure().sender(sender),
                    core.getMessage("teleportask.deny", List.of(playerData.lastRequest.host.getName())));
            Utilities.sendColorText(core.getAdventure().sender(playerData.lastRequest.host),
                    core.getMessage("teleportask.deny", List.of(sender.getName())));
            playerData.lastRequest = null;
            return true;
        } else {
            Utilities.sendColorText(core.getAdventure().sender(sender), core.getMessage("generic.noconsole"));
            return false;
        }
    }

}
