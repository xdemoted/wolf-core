package risingdeathx2.spigot.wolfcore.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import risingdeathx2.spigot.wolfcore.core;
import risingdeathx2.spigot.wolfcore.utils;
import risingdeathx2.spigot.wolfcore.classes.Argument;
import risingdeathx2.spigot.wolfcore.classes.ArgumentType;
import risingdeathx2.spigot.wolfcore.classes.Command;
import risingdeathx2.spigot.wolfcore.classes.CoreCommandExecutor;

public class delwarp implements CoreCommandExecutor {
    @Override
    public Command getCommand() {
        return new Command("delwarp","wolfcore.delwarp", new ArrayList<>() {{
            new Argument("warp", ArgumentType.WARP, true);
        }});
    }

    core core;

    public delwarp(core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        boolean result = core.warps.remove(args[0]);
        if (result) {
            utils.sendColorText(core.adventure().sender(sender), core.getMessage("warp.deleted", List.of(args[0])));
        } else {
            utils.sendColorText(core.adventure().sender(sender), core.getMessage("warp.notfound", List.of(args[0])));
        }
        try {
            core.warps.save();
        } catch (Exception e) {
        }
        return result;
    }
    
}
