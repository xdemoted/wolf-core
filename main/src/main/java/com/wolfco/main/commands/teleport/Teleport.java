package com.wolfco.main.commands.teleport;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.commands.arguments.PlayerArg;
import com.wolfco.main.utility.FontUtil;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import jakarta.inject.Singleton;

@Singleton
public class Teleport implements CoreCommand {
    @Override
    public Command getCommand() {
        Command command = new Command().setName("teleport");
        command.addArguments(
                new PlayerArg(true).includeSender(false).setName("PLAYER1"),
                new PlayerArg(false).includeSender(false).setName("PLAYER2")
                );
        command.addAliases("tp");
        return command;
    }
    
    @Override
    public boolean onCommand(CommandSourceStack commandStack, String[] args,
            Object[] argumentValues) {
        CommandSender sender = commandStack.getSender();

        Player player1 = (Player) argumentValues[0];
        Player player2 = (Player) argumentValues[1];
        
        Boolean console = false;

        if (!(sender instanceof Player)) {
            console = true;
        }

        if (player2 == null) {
            if (console) {
                getMessageUtility().sendPreset(sender, "generic.consoleargs", List.of("2"));
                return false;
            }

            if (player1.getUniqueId() == ((Player) sender).getUniqueId()) {
                getMessageUtility().sendPreset(sender, "teleport.self");
                return false;
            }

            ((Player) sender).teleport(player1);
            getMessageUtility().sendPreset(sender, "teleport.success", List.of(FontUtil.getPlayerTag(player1)));
        } else {
            if (player1.getUniqueId() == player2.getUniqueId()) {
                getMessageUtility().sendPreset(sender, "teleport.self");
                return false;
            } else {
                player1.teleport(player2);
            }

            getMessageUtility().sendPreset(sender, "teleport.othersuccess", List.of(FontUtil.getPlayerTag(player1), FontUtil.getPlayerTag(player2)));
            return true;
        }
        return true;
    }
}
