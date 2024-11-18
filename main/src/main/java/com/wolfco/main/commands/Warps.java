package com.wolfco.main.commands;

import com.wolfco.common.Utilities;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.main.Core;

import net.kyori.adventure.audience.Audience;

public class Warps implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("warps");
        command.setDescription("Lists available warps.");
        command.setNode("wolfcore.warps");

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public Warps(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String alias,
            String[] args, Object[] argumentValues) {
        Audience audience = core.getAdventure().sender(sender);
        Utilities.sendColorText(audience, "<#ffaa00>Warps:");
        for (String key : core.warps.getRoutesAsStrings(false)) {
            Utilities.sendColorText(audience,
                    "<#ffaa00> - <#ffff00><click:run_command:/warp " + key + ">" + key + "</click>");
        }
        return true;
    }
}
