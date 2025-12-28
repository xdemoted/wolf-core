package com.wolfco.common.commands;

import java.util.List;
import java.util.logging.Level;

import org.bukkit.command.PluginCommand;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.classes.CorePlugin;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class CommandLoader {
    @Inject
    public CorePlugin core;

    @Inject
    List<CoreCommand> commands;

    public void register(CoreCommand executor) {
        Command command = executor.getCommand();
        String name = command.getName();

        PluginCommand pluginCommand = core.getCommand(name);

        if (pluginCommand != null) {
            pluginCommand.setExecutor(executor);
            pluginCommand.setTabCompleter(executor);
        } else
            core.getLogger().log(Level.WARNING, "Command {0} cannot be found in the plugin yml.", name);
    }

    public void registerAll() {
        commands.forEach(this::register);
    }
}
