package risingdeathx2.spigot.wolfcore.commands;

import java.util.ArrayList;

import net.kyori.adventure.audience.Audience;
import risingdeathx2.spigot.wolfcore.core;
import risingdeathx2.spigot.wolfcore.utils;
import risingdeathx2.spigot.wolfcore.classes.Command;
import risingdeathx2.spigot.wolfcore.classes.CoreCommandExecutor;

public class warps implements CoreCommandExecutor {
    @Override
    public Command getCommand() {
        return new Command("warps","wolfcore.warp", new ArrayList<>());
    }

    core core;
    public warps(core core) {
        this.core = core;
    }

    @Override
    public boolean execute(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        Audience audience = core.adventure().sender(sender);
        utils.sendColorText(audience, "<#ffaa00>Warps:");
        for (String key : core.warps.getRoutesAsStrings(false)) {
            utils.sendColorText(audience, "<#ffaa00> - <#ffff00>" + key);
        }
        return true;
    }
}
