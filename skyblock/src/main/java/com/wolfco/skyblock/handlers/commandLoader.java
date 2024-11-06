package com.wolfco.skyblock.handlers;

import org.bukkit.command.PluginCommand;
import com.wolfco.skyblock.main;
import com.wolfco.skyblock.classes.Command;
import com.wolfco.skyblock.classes.CoreCommandExecutor;
import java.util.HashMap;

public class commandLoader {
    public main main;
    public HashMap<String, CoreCommandExecutor> commands = new HashMap<>();

    public commandLoader(main main) {
        this.main = main;
    }

    public void loadCommands() {

    }

    public void register(CoreCommandExecutor executor) {
        Command command = executor.getCommand();
        commands.put(command.name, executor);
        PluginCommand PluginCommand = main.getCommand(command.name);
        if (PluginCommand != null)
            PluginCommand.setExecutor(executor);
        else
            main.getLogger().warning("Command " + command.name + " is not registered.");
    }

    /*
     * The CoreCommandExecutor automatically handles the following situations:
     * - Checking if the player has the required permission to execute the command
     * - Checking if the player has the required number of arguments
     * - Checking if the player has too many arguments
     * - Displaying the correct usage of the command
     */
}
