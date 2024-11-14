package com.wolfco.common;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.command.PluginCommand;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.CorePlugin;

public class CommandLoader {
    public CorePlugin core;
    public HashMap<String, CoreCommandExecutor> commands = new HashMap<>();
    public List<CoreCommandExecutor> executors = new ArrayList<>();

    public CommandLoader(CorePlugin core) {
        this.core = core;
    }

    public void register(CoreCommandExecutor executor) {
        Command command = executor.getCommand();
        commands.put(command.name, executor);
        PluginCommand PluginCommand = core.getCommand(command.name);
        if (PluginCommand != null) {
            PluginCommand.setExecutor(executor);
            PluginCommand.setTabCompleter(executor);
        }
        else core.getLogger().log(Level.WARNING, "Command {0} cannot be found in the plugin yml.", command.name);
    }

    public void registerAll(List<CoreCommandExecutor> executors) {
        executors.forEach(executor -> register(executor));
    }
}
