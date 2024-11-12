package com.wolfco.main.commands;

import java.util.ArrayList;

import net.kyori.adventure.audience.Audience;
import com.wolfco.main.core;
import com.wolfco.common.utils;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;

public class warps implements CoreCommandExecutor {
    @Override
    public Command getCommand() {
        return new Command("warps","wolfcore.warp", new ArrayList<>());
    }

    @Override
    public core fetchCore() {
        return core;
    }
    
    core core;
    public warps(core core) {
        this.core = core;
    }

    @Override
    public boolean execute(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        Audience audience = core.getAdventure().sender(sender);
        utils.sendColorText(audience, "<#ffaa00>Warps:");
        for (String key : core.warps.getRoutesAsStrings(false)) {
            utils.sendColorText(audience, "<#ffaa00> - <#ffff00>" + key);
        }
        return true;
    }
}
