package com.wolfco.main.commands;

import java.io.FileNotFoundException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.kyori.adventure.audience.Audience;
import com.wolfco.main.core;
import com.wolfco.common.utils;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;

public class database implements CoreCommandExecutor {
    @Override
    public Command getCommand() {
        Command command = new Command("database");
        command.setDescription("Manage the schematic database");
        command.setNode("wolfcore.database");
        return command;
    }

    @Override
    public core fetchCore() {
        return core;
    }

    core core;
    public database(core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        Audience audience = core.adventure().sender(sender);
        if (!(sender instanceof Player)) {
            utils.sendColorText(audience, core.getMessage("generic.noconsole"));
            return true;
        }

        if (args[0].equals("add")) {
            if (!(args.length == 2)) {
                utils.sendColorText(audience, "<#ffaa00>Usage: /database add <name>");
                return true;
            }

            try {
                core.db.addSchematic(args[1]);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (args[0].equals("remove")) {
            if (!(args.length == 2)) {
                utils.sendColorText(audience, "<#ffaa00>Usage: /database remove <name>");
                return true;
            }

            core.db.removeSchematic(args[1]);
        } else if (args[0].equals("list")) {
            for (String name : core.db.listSchematics()) {
                utils.sendColorText(audience, "<#ffaa00>" + name);
            }
        } else {
            utils.sendColorText(audience, "<#ffaa00>Usage: /database <add/remove/list> <name>");
        } 
        return true;
    }
}
