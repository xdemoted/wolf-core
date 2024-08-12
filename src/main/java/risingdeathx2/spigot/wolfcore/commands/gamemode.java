package risingdeathx2.spigot.wolfcore.commands;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import risingdeathx2.spigot.wolfcore.core;
import risingdeathx2.spigot.wolfcore.utils;

public class gamemode {
    static HashMap<String, GameMode> aliases = new HashMap<String, GameMode>() {
        {
            put("gmc", GameMode.CREATIVE);
            put("gms", GameMode.SURVIVAL);
            put("gmsp", GameMode.SPECTATOR);
            put("gma", GameMode.ADVENTURE);
        }
    };
    static HashMap<String, GameMode> modes = new HashMap<String, GameMode>() {
        {
            put("creative", GameMode.CREATIVE);
            put("survival", GameMode.SURVIVAL);
            put("spectator", GameMode.SPECTATOR);
            put("adventure", GameMode.ADVENTURE);
        }
    };
    public gamemode(core core, Player player, String alias, String[] args) {
        utils utils = new utils(core);
        GameMode mode = null;
        Collection<Player> target = new ArrayList<>();
        if (player != null) {
            target.add(player);
        }
        if (aliases.containsKey(alias)) {
            mode = aliases.get(alias);
            if (args.length > 0) {
                target = utils.getTargets(args[0].toLowerCase());
            }
        } else {
            if (args.length > 0) {
                String targetMode = args[0].toLowerCase();
                if (modes.containsKey(targetMode)) {
                    mode = modes.get(targetMode);
                    if (args.length > 1) {
                        target = utils.getTargets(args[1]);
                    }
                } else {
                    utils.sendPlayer(player, risingdeathx2.spigot.wolfcore.core.prefix + "<#ff5555>Invalid gamemode.");
                    return;
                }
            } else {
                utils.sendPlayer(player, risingdeathx2.spigot.wolfcore.core.prefix + "<#ff5555>/gm <creative|survival|spectator|adventure> [target]");
                return;
            }
        }
        if (target == null||target.isEmpty()) {
            utils.sendPlayer(player, risingdeathx2.spigot.wolfcore.core.prefix + "<#ff5555>Invalid target.");
            return;
        }
        for (Player p : target) {
            p.setGameMode(mode);
        }
        if (target.size() == 1&&target.contains(player)) {
            utils.sendPlayer(player, risingdeathx2.spigot.wolfcore.core.prefix + "<#55ff55>Set gamemode to " + mode.toString().toLowerCase() + ".");
        } else if (target.size() == 1) {
            for (Player p : target) {
                utils.sendPlayer(player, risingdeathx2.spigot.wolfcore.core.prefix + "<#55ff55>Set " + p.getName() + "'s gamemode to " + mode.toString().toLowerCase() + ".");
            }
        } else {
            utils.sendPlayer(player, risingdeathx2.spigot.wolfcore.core.prefix + "<#55ff55>Set " + target.size() + " players' gamemode to " + mode.toString().toLowerCase() + ".");
        }
    }
}
