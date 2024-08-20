package risingdeathx2.spigot.wolfcore.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import risingdeathx2.spigot.wolfcore.core;
import risingdeathx2.spigot.wolfcore.utils;
import risingdeathx2.spigot.wolfcore.classes.Argument;
import risingdeathx2.spigot.wolfcore.classes.ArgumentType;
import risingdeathx2.spigot.wolfcore.classes.Command;
import risingdeathx2.spigot.wolfcore.classes.CoreCommandExecutor;

public class setwarp implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        return new Command("setwarp", new ArrayList<>() {{
            add(new Argument("warp", ArgumentType.STRING, false));
        }});
    }
    core core;
    public setwarp(core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        String warpName = args[0];
        if (!(sender instanceof Player)) {
            utils.sendColorText(core.adventure().sender(sender), core.getMessage("generic.noconsole"));
            return false;
        }
        Player player = (Player) sender;
        if (!warpName.matches("^[a-zA-Z0-9]+$")) {
            utils.sendColorText(core.adventure().sender(sender),core.getMessage("generic.alphanumeric", List.of("warp name")));
            return true;
        } else if (core.warps.contains(warpName)) {
            utils.sendColorText(core.adventure().sender(sender),core.getMessage("warp.exists", List.of(warpName)));
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
        utils.sendColorText(core.adventure().sender(sender),core.getMessage("warp.set", List.of(warpName)));
        return true;
    }
    
}
