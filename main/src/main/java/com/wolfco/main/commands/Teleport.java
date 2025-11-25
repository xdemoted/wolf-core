package com.wolfco.main.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.argumenthandlers.PlayerArg;
import com.wolfco.main.Core;
import com.wolfco.main.utility.FontUtil;

public class Teleport implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("teleport");
        command.addArguments(
                new PlayerArg(true).includeSender(false).setName("PLAYER1"),
                new PlayerArg(false).includeSender(false).setName("PLAYER2")
                );

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public Teleport(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String label, String[] args,
            Object[] argumentValues) {
        Player player1 = (Player) argumentValues[0];
        Player player2 = (Player) argumentValues[1];
        
        Boolean console = false;

        if (!(sender instanceof Player)) {
            console = true;
        }

        if (player2 == null) {
            if (console) {
                core.sendPreset(sender, "generic.consoleargs", List.of("2"));
                return false;
            }

            if (player1.getUniqueId() == ((Player) sender).getUniqueId()) {
                core.sendPreset(sender, "teleport.self");
                return false;
            }

            ((Player) sender).teleport(player1);
            core.sendPreset(sender, "teleport.success", List.of(FontUtil.getPlayerTag(player1)));
        } else {
            if (player1.getUniqueId() == player2.getUniqueId()) {
                core.sendPreset(sender, "teleport.self");
                return false;
            } else {
                player1.teleport(player2);
            }

            core.sendPreset(sender, "teleport.othersuccess", List.of(FontUtil.getPlayerTag(player1), FontUtil.getPlayerTag(player2)));
            return true;
        }
        return true;
    }
}
