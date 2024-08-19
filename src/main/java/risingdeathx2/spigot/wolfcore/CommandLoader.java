package risingdeathx2.spigot.wolfcore;

import java.util.HashMap;

import org.bukkit.command.PluginCommand;

import risingdeathx2.spigot.wolfcore.classes.Command;
import risingdeathx2.spigot.wolfcore.classes.CoreCommandExecutor;
import risingdeathx2.spigot.wolfcore.commands.gamemode;
import risingdeathx2.spigot.wolfcore.commands.gamemodeAlias;
import risingdeathx2.spigot.wolfcore.commands.teleport;
import risingdeathx2.spigot.wolfcore.commands.teleportaccept;
import risingdeathx2.spigot.wolfcore.commands.teleportall;
import risingdeathx2.spigot.wolfcore.commands.teleportask;
import risingdeathx2.spigot.wolfcore.commands.teleportdeny;
import risingdeathx2.spigot.wolfcore.commands.teleporthere;

public class CommandLoader {
    public core core;
    public HashMap<String, CoreCommandExecutor> commands = new HashMap<>();

    public CommandLoader(core core) {
        this.core = core;
    }

    public void loadCommands() {
        register(new gamemode(core));
        register(new gamemodeAlias(core));
        register(new teleport(core));
        register(new teleportall(core));
        register(new teleporthere(core));
        register(new teleportaccept(core));
        register(new teleportask(core));
        register(new teleportdeny(core));
    }

    public void register(CoreCommandExecutor executor) {
        Command command = executor.getCommand();
        commands.put(command.name, executor);
        PluginCommand PluginCommand = core.getCommand(command.name);
        if (PluginCommand != null) PluginCommand.setExecutor(executor);
        else core.getLogger().warning("Command " + command.name + " is not registered.");
    }
}
