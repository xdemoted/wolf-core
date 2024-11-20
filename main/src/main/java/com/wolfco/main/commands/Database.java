package com.wolfco.main.commands;

import java.io.FileNotFoundException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.Utilities;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.main.Core;

import net.kyori.adventure.audience.Audience;

public class Database implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("database");
        command.setDescription("Manage the schematic database");
        command.setNode("wolfcore.database");
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
        Audience audience = core.getAdventure().sender(sender);
        if (!(sender instanceof Player)) {
            Utilities.sendColorText(audience, core.getMessage("generic.noconsole"));
            return true;
        }

        switch (args[0]) {
            case "add" -> {
                if (args.length != 2) {
                    Utilities.sendColorText(audience, "<#ffaa00>Usage: /database add <name>");
                    return true;
                }   try {
                    core.getDatabaseHandler().addSchematic(args[1]);
                } catch (FileNotFoundException e) {
                    Utilities.sendColorText(audience, "<#ffaa00>File not found");
                }
            }
            case "remove" -> {
                if (args.length != 2) {
                    Utilities.sendColorText(audience, "<#ffaa00>Usage: /database remove <name>");
                    return true;
                }   core.getDatabaseHandler().removeSchematic(args[1]);
            }
            case "list" -> {
                for (String name : core.getDatabaseHandler().listSchematics()) {
                    Utilities.sendColorText(audience, "<#ffaa00>" + name);
                }
            }
            default -> Utilities.sendColorText(audience, "<#ffaa00>Usage: /database <add/remove/list> <name>");
        }
        return true;
    }
}
