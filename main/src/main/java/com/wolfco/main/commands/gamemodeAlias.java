package com.wolfco.main.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
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
    public Command getCommand() {
        return new Command("gms","wolfcore.gamemode", new ArrayList<Argument>() {
            {
                add(new Argument("player", ArgumentType.PLAYER, true));
            }
        });
    }

    @Override
    public Core fetchCore() {
        return core;
    }
    
    CoreCommandExecutor executor;
    Core core;
    public gamemodeAlias(@Nonnull Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        if (sender instanceof Player) {
            Audience senderAudience = core.getAdventure().sender(sender);
            Player player = (Player) sender;
            GameMode mode = gamemodes.get(alias);
            if (mode == null) {
                Utilities.sendColorText(senderAudience, core.getMessage("gamemode.invalid"));
                return false;
            }
            if (args.length == 1) {
                List<Player> players = getCommand().options.get(0).getPlayer(core, args[0]);
                if (players != null) {
                    for (Player target : players) {
                        target.setGameMode(mode);
                    }
                    if (players.size() > 1) {
                        Utilities.sendColorText(senderAudience, core.getMessage("gamemode.multisuccess", List.of(String.valueOf(players.size()), mode.toString())));
                        return true;
                    } else if (players.contains(player)) {
                        Utilities.sendColorText(senderAudience, core.getMessage("gamemode.selfsuccess", List.of(mode.toString())));
                        return true;
                    }
                    Utilities.sendColorText(senderAudience, core.getMessage("gamemode.othersuccess", List.of(mode.toString())));
                    return true;
                } else {
                    Utilities.sendColorText(senderAudience, core.getMessage("generic.playernotfound"));
                }
            } else {
                player.setGameMode(mode);
                Utilities.sendColorText(senderAudience, core.getMessage("gamemode.selfsuccess", List.of(mode.toString())));
                return false;
            }
        } else if (args.length == 1) {
            List<Player> players = getCommand().options.get(0).getPlayer(core, args[0]);
            if (players != null) {
                GameMode mode = gamemodes.get(alias);
                if (mode == null) {
                    Utilities.sendColorText(core.getAdventure().sender(sender), core.getMessage("gamemode.invalid"));
                    return false;
                }
                for (Player player : players) {
                    player.setGameMode(mode);
                }
                return true;
            } else {
                Utilities.sendColorText(core.getAdventure().sender(sender), core.getMessage("generic.playernotfound"));
            }
        }
        sender.sendMessage("A player must be specified.");
        return false;
    }
    
    
}
