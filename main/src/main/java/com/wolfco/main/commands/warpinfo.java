package com.wolfco.main.commands;

import java.util.List;

import org.bukkit.World;

import com.wolfco.main.Core;
import com.wolfco.main.classes.Warp;
<<<<<<< HEAD
import com.wolfco.main.classes.customArgs.WarpArgument;
import com.wolfco.common.Utilities;
=======
import com.wolfco.main.classes.customArgs.WarpArg;
import com.wolfco.common.utils;
>>>>>>> 176256cb29a2aceab31316a77fe99b1426fa0668
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;

public class warpinfo implements CoreCommandExecutor {
    @Override
    public Command getCommand() {
        Command command = new Command("warpinfo");
<<<<<<< HEAD
        command.setDescription("Get information about a warp");
        command.setNode("wolfcore.warpinfo");
        command.addArgument(new WarpArgument(true));
=======
        command.setDescription("Gets information about a warp.");
        command.setNode("wolfcore.warpinfo");
        command.addOption(new WarpArg(true));

>>>>>>> 176256cb29a2aceab31316a77fe99b1426fa0668
        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }
    
    Core core;
    public warpinfo(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        Warp warp;

        try {
<<<<<<< HEAD
            warp = (Warp) getCommand().getArgument(0).getValue(core, sender, command, args[0]);
        } catch (Exception e) {
=======
            warp = (Warp) getArgument(0).getValue(core, sender, command, args[0]);
        } catch (Exception e) {
            return false;
        }
        
        if (warp == null) {
            utils.sendColorText(core.getAdventure().sender(sender), core.getMessage("warp.notfound", List.of(args[0])));
>>>>>>> 176256cb29a2aceab31316a77fe99b1426fa0668
            return false;
        }
        
        World world = core.getServer().getWorld(warp.world);
        String worldName;
        if (world == null) {
            worldName = warp.world.toString();
        } else {
            worldName = world.getName();
        }
<<<<<<< HEAD
        Utilities.sendColorText(core.getAdventure().sender(sender), core.getMessage("warp.info", List.of(warp.name, worldName, String.valueOf(warp.x), String.valueOf(warp.y), String.valueOf(warp.z))));
=======
        utils.sendColorText(core.getAdventure().sender(sender), core.getMessage("warp.info", List.of(warp.name, worldName, String.valueOf(warp.x), String.valueOf(warp.y), String.valueOf(warp.z))));
>>>>>>> 176256cb29a2aceab31316a77fe99b1426fa0668
        return true;
    }
}
