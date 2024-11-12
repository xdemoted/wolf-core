package com.wolfco.main.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import com.wolfco.main.Core;
import com.wolfco.main.classes.customArgs.WarpArgument;
import com.wolfco.common.Utilities;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;

public class delwarp implements CoreCommandExecutor {
    @Override
    public Command getCommand() {
        Command command = new Command("delwarp");
        command.setDescription("Used to delete warps");
        command.setNode("wolfcore.delwarp");
        command.setArguments(new ArrayList<>() {{
            add(new WarpArgument(true));
        }});

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }
    
    Core core;
    public delwarp(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        boolean result = core.warps.remove(args[0]);
        if (result) {
<<<<<<< HEAD
            Utilities.sendColorText(core.getAdventure().sender(sender), core.getMessage("warp.deleted", List.of(args[0])));
        } else {
            Utilities.sendColorText(core.getAdventure().sender(sender), core.getMessage("warp.notfound", List.of(args[0])));
=======
            utils.sendColorText(core.getAdventure().sender(sender), core.getMessage("warp.deleted", List.of(args[0])));
        } else {
            utils.sendColorText(core.getAdventure().sender(sender), core.getMessage("warp.notfound", List.of(args[0])));
>>>>>>> 176256cb29a2aceab31316a77fe99b1426fa0668
        }
        try {
            core.warps.save();
        } catch (Exception e) {
        }
        return result;
    }
    
}
