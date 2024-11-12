package com.wolfco.main.commands;

import net.kyori.adventure.audience.Audience;
import com.wolfco.main.Core;
import com.wolfco.common.Utilities;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;

public class warps implements CoreCommandExecutor {
    @Override
    public Command getCommand() {
        Command command = new Command("warps");
<<<<<<< HEAD
        command.setDescription("List all warps");
=======
        command.setDescription("Lists available warps.");
>>>>>>> 176256cb29a2aceab31316a77fe99b1426fa0668
        command.setNode("wolfcore.warps");

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public warps(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String alias,
            String[] args) {
        Audience audience = core.getAdventure().sender(sender);
        Utilities.sendColorText(audience, "<#ffaa00>Warps:");
        for (String key : core.warps.getRoutesAsStrings(false)) {
            Utilities.sendColorText(audience,
                    "<#ffaa00> - <#ffff00><click:run_command:/warp " + key + ">" + key + "</click>");
        }
        return true;
    }
}
