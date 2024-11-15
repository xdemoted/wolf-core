package com.wolfco.main.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import net.kyori.adventure.audience.Audience;

import com.wolfco.main.Core;
import com.wolfco.common.Utilities;
import com.wolfco.common.classes.Argument;
import com.wolfco.common.classes.ArgumentType;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;

public class gamemodeAlias implements CoreCommandExecutor {

    final HashMap<String, GameMode> gamemodes = new HashMap<String, GameMode>() {
        {
            put("gms", GameMode.SURVIVAL);
            put("gmc", GameMode.CREATIVE);
            put("gma", GameMode.ADVENTURE);
            put("gmsp", GameMode.SPECTATOR);
        }
    };

    @Override
    public Command getCommand() {
        Command command = new Command("gms");
        command.setDescription("Used to modify player's gamemode.");
        command.setNode("wolfcore.gamemode");
        command.setArguments(new ArrayList<>() {
            {
                add(new Argument(ArgumentType.OTHERPLAYER, false));
            }
        });

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public gamemodeAlias(@Nonnull Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        Collection<Player> target = (Collection<Player>) argumentValues[0];
        Audience senderAudience = core.getAdventure().sender(sender);
        GameMode mode = gamemodes.get(alias);

        if (mode == null) {
            Utilities.sendColorText(senderAudience, core.getMessage("gamemode.invalid"));
            return false;
        }

        if (target != null) {
            for (Player tempPlayer : target) {
                tempPlayer.setGameMode(mode);
            }

            if (target.size() > 1) {
                Utilities.sendColorText(senderAudience, core.getMessage("gamemode.multisuccess", List.of(String.valueOf(target.size()), mode.toString())));
                return true;
            }

            Utilities.sendColorText(senderAudience, core.getMessage("gamemode.othersuccess", List.of(mode.toString())));
            return true;
        } else {
            if (sender instanceof Player player) {
                player.setGameMode(mode);
                Utilities.sendColorText(senderAudience, core.getMessage("gamemode.selfsuccess", List.of(mode.toString())));
                return false;
            } else if (sender instanceof ConsoleCommandSender) {
                Utilities.sendColorText(core.getAdventure().sender(sender), core.getMessage("generic.consoleargs", List.of("1")));
                return false;
            }
        }
        return false;
    }

}
