package risingdeathx2.spigot.wolfcore.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.kyori.adventure.audience.Audience;
import risingdeathx2.spigot.wolfcore.core;
import risingdeathx2.spigot.wolfcore.utils;
import risingdeathx2.spigot.wolfcore.classes.Argument;
import risingdeathx2.spigot.wolfcore.classes.ArgumentType;
import risingdeathx2.spigot.wolfcore.classes.Command;
import risingdeathx2.spigot.wolfcore.classes.CoreCommandExecutor;

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
        return new Command("gms", new ArrayList<Argument>() {
            {
                add(new Argument("player", ArgumentType.PLAYER, true));
            }
        });
    }
    CoreCommandExecutor executor;
    core core;
    public gamemodeAlias(@Nonnull core core) {
        this.core = core;
    }
    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        if (sender instanceof Player) {
            Audience senderAudience = core.adventure().sender(sender);
            Player player = (Player) sender;
            GameMode mode = gamemodes.get(alias);
            if (mode == null) {
                utils.sendColorText(senderAudience, core.getMessage("gamemode.invalid"));
                return false;
            }
            if (args.length == 1) {
                List<Player> players = getCommand().options.get(0).getPlayer(core, args[0]);
                if (players != null) {
                    for (Player target : players) {
                        target.setGameMode(mode);
                    }
                    if (players.size() > 1) {
                        utils.sendColorText(senderAudience, core.getMessage("gamemode.multisuccess", List.of(String.valueOf(players.size()), mode.toString())));
                        return true;
                    } else if (players.contains(player)) {
                        utils.sendColorText(senderAudience, core.getMessage("gamemode.selfsuccess", List.of(mode.toString())));
                        return true;
                    }
                    utils.sendColorText(senderAudience, core.getMessage("gamemode.othersuccess", List.of(mode.toString())));
                    return true;
                } else {
                    utils.sendColorText(senderAudience, core.getMessage("generic.playernotfound"));
                }
            } else {
                player.setGameMode(mode);
                utils.sendColorText(senderAudience, core.getMessage("gamemode.selfsuccess", List.of(mode.toString())));
                return false;
            }
        } else if (args.length == 1) {
            List<Player> players = getCommand().options.get(0).getPlayer(core, args[0]);
            if (players != null) {
                GameMode mode = gamemodes.get(alias);
                if (mode == null) {
                    utils.sendColorText(core.adventure().sender(sender), core.getMessage("gamemode.invalid"));
                    return false;
                }
                for (Player player : players) {
                    player.setGameMode(mode);
                }
                return true;
            }
        }
        sender.sendMessage("A player must be specified.");
        return false;
    }
    
    
}
