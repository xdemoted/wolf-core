package com.wolfco.main.commands;

import java.io.FileNotFoundException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.main.Core;

public class Database implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("database");
        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public Database(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {

        if (!(sender instanceof Player)) {
            core.sendPreset(sender, "generic.noconsole");
            return true;
        }

        switch (args[0]) {
            case "add" -> {
                if (args.length != 2) {
                    core.sendMessage(sender, "<#ffaa00>Usage: /database add <name>");
                    return true;
                }   try {
                    core.getDatabaseHandler().addSchematic(args[1]);
                } catch (FileNotFoundException e) {
                    core.sendMessage(sender, "<#ffaa00>File not found");
                }
            }
            case "remove" -> {
                if (args.length != 2) {
                    core.sendMessage(sender, "<#ffaa00>Usage: /database remove <name>");
                    return true;
                }   core.getDatabaseHandler().removeSchematic(args[1]);
            }
            case "list" -> {
                for (String name : core.getDatabaseHandler().listSchematics()) {
                    core.sendMessage(sender, "<#ffaa00>" + name);
                }
            }
            default -> core.sendMessage(sender, "<#ffaa00>Usage: /database <add/remove/list> <name>");
        }
        return true;
    }
}
