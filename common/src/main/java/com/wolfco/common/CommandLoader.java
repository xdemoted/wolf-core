package com.wolfco.common;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.command.PluginCommand;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.CorePlugin;

public class CommandLoader {
     CorePlugin core;
     HashMap<String, CoreCommandExecutor> commands = new HashMap<>();
     
    public CommandLoader(CorePlugin core) {
        this.core = core;
    }

    public void register(CoreCommandExecutor executor) {
        Command command = executor.getCommand();
        String name = command.getName();

        commands.put(name, executor);
        PluginCommand pluginCommand = core.getCommand(name);
        if (pluginCommand != null) {
            pluginCommand.setExecutor(executor);
            pluginCommand.setTabCompleter(executor);
        }
        else core.getLogger().log(Level.WARNING, "Command {0} cannot be found in the plugin yml.", name);
    }

    public void registerAll(List<CoreCommandExecutor> executors) {
        executors.forEach(this::register);
    }
}
