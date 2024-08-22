package risingdeathx2.spigot.wolfcore.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;

import risingdeathx2.spigot.wolfcore.core;
import risingdeathx2.spigot.wolfcore.utils;
import risingdeathx2.spigot.wolfcore.classes.Argument;
import risingdeathx2.spigot.wolfcore.classes.ArgumentType;
import risingdeathx2.spigot.wolfcore.classes.Command;
import risingdeathx2.spigot.wolfcore.classes.CoreCommandExecutor;
import risingdeathx2.spigot.wolfcore.classes.Warp;

public class warpinfo implements CoreCommandExecutor {
    @Override
    public Command getCommand() {
        return new Command("warpinfo","wolfcore.warpinfo", new ArrayList<>() {{
            new Argument("warp", ArgumentType.WARP, true);
        }});
    }

    core core;
    public warpinfo(core core) {
        this.core = core;
    }

    @Override
    public boolean execute(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        Warp warp = getCommand().options.get(0).getWarp(core, args[0]);
        if (warp == null) {
            utils.sendColorText(core.adventure().sender(sender), core.getMessage("warp.notfound", List.of(args[0])));
            return false;
        }
        World world = core.getServer().getWorld(warp.world);
        String worldName;
        if (world == null) {
            worldName = warp.world.toString();
        } else {
            worldName = world.getName();
        }
        utils.sendColorText(core.adventure().sender(sender), core.getMessage("warp.info", List.of(warp.name, worldName, String.valueOf(warp.x), String.valueOf(warp.y), String.valueOf(warp.z))));
        return true;
    }
}
