package com.wolfco.main.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.main.Core;
import com.wolfco.common.Utilities;
import com.wolfco.common.classes.Argument;
import com.wolfco.common.classes.ArgumentType;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;

public class setwarp implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        return new Command("setwarp","wolfcore.setwarp", new ArrayList<>() {{
            add(new Argument("warp", ArgumentType.STRING, false));
        }});
    }

    @Override
    public Core fetchCore() {
        return core;
    }
    
    Core core;
    public setwarp(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        String warpName = args[0];
        if (!(sender instanceof Player)) {
            Utilities.sendColorText(core.getAdventure().sender(sender), core.getMessage("generic.noconsole"));
            return false;
        }
        Player player = (Player) sender;
        if (!warpName.matches("^[a-zA-Z0-9]+$")) {
            Utilities.sendColorText(core.getAdventure().sender(sender),core.getMessage("generic.alphanumeric", List.of("warp name")));
            return true;
        } else if (core.warps.contains(warpName)) {
            Utilities.sendColorText(core.getAdventure().sender(sender),core.getMessage("warp.exists", List.of(warpName)));
            return true;
        }
        core.warps.set(warpName+".x",player.getLocation().getX());
        core.warps.set(warpName+".y",player.getLocation().getY());
        core.warps.set(warpName+".z",player.getLocation().getZ());
        core.warps.set(warpName+".world",player.getLocation().getWorld().getUID().toString());
        try {
            core.warps.save();
        } catch (Exception e) {
        }
        Utilities.sendColorText(core.getAdventure().sender(sender),core.getMessage("warp.set", List.of(warpName)));
        return true;
    }
    
}
