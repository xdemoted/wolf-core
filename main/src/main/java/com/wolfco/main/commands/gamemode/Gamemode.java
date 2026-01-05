package com.wolfco.main.commands.gamemode;

import java.util.Collection;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.MessageUtility;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.commands.arguments.GameModeArg;
import com.wolfco.common.commands.arguments.MultiPlayerArg;
import com.wolfco.main.utility.FontUtil;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class Gamemode implements CoreCommand {
    @Override
    public Command getCommand() {
        Command command = new Command().setName("gamemode");
        command.addArguments(
                new GameModeArg(true),
                new MultiPlayerArg(false).includeSender(false)
        );
        command.addAliases("gm");

        return command;
    }

    @Inject
    MessageUtility messageUtility;
    
    @Override
    public boolean onCommand(CommandSourceStack commandStack, String[] args, Object[] argumentValues) {
        GameMode mode = (GameMode) argumentValues[0];
        CommandSender sender = commandStack.getSender();
        
        @SuppressWarnings("unchecked")
        Collection<Player> target = (Collection<Player>) argumentValues[1];

        Boolean console = (sender instanceof ConsoleCommandSender);

        if (console && args.length == 1) {
            messageUtility.sendPreset(sender, "generic.consoleargs", List.of("2"));
            return false;
        }

        if (args.length == 1) {

            if (console) {
                messageUtility.sendPreset(sender, "generic.consoleargs", List.of("2"));
                return false;
            }

            ((Player) sender).setGameMode(mode);
            messageUtility.sendPreset(sender, "gamemode.selfsuccess", List.of(mode.toString()));
            return true;
        } else if (target instanceof Collection) {
            if (target.size() == 1) {
                messageUtility.sendPreset(sender, "gamemode.othersuccess", List.of(FontUtil.getPlayerTag(target.iterator().next()), mode.toString()));
            } else {
                messageUtility.sendPreset(sender, "gamemode.multisuccess", List.of(String.valueOf(target.size()), mode.toString()));
            }

            for (Player p : target) {
                p.setGameMode(mode);
            }

            return true;
        }
        return true;
    }
}
