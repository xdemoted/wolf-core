package risingdeathx2.spigot.wolfcore.commands;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.Collection;

import risingdeathx2.spigot.wolfcore.core;
import risingdeathx2.spigot.wolfcore.utils;

public class gamemode {
    @SuppressWarnings("static-access")
    public gamemode(core core, Player player, String alias, String[] args) {
        utils utils = new utils(core);
        GameMode mode = null;
        Collection<Player> target = new ArrayList<>();
        if (player != null) {
            target.add(player);
        }
        if (alias.equalsIgnoreCase("gmc")) {
            mode = GameMode.CREATIVE;
            if (args.length > 0) {
                target = utils.getTargets(args[0]);
            }
        } else if (alias.equalsIgnoreCase("gms")) {
            mode = GameMode.SURVIVAL;
            if (args.length > 0) {
                target = utils.getTargets(args[0]);
            }
        } else if (alias.equalsIgnoreCase("gmsp")) {
            mode = GameMode.SPECTATOR;
            if (args.length > 0) {
                target = utils.getTargets(args[0]);
            }
        } else if (alias.equalsIgnoreCase("gma")) {
            mode = GameMode.ADVENTURE;
            if (args.length > 0) {
                target = utils.getTargets(args[0]);
            }
        } else {
            if (args.length > 0) {
                if ("creative".startsWith(args[0].toLowerCase())) {
                    mode = GameMode.CREATIVE;
                    if (args.length > 1) {
                        target = utils.getTargets(args[0]);
                    }
                } else if ("survival".startsWith(args[0].toLowerCase())) {
                    mode = GameMode.SURVIVAL;
                    if (args.length > 1) {
                        target = utils.getTargets(args[0]);
                    }
                } else if ("spectator".startsWith(args[0].toLowerCase())) {
                    mode = GameMode.SPECTATOR;
                    if (args.length > 1) {
                        target = utils.getTargets(args[0]);
                    }
                } else if ("adventure".startsWith(args[0].toLowerCase())) {
                    mode = GameMode.ADVENTURE;
                    if (args.length > 1) {
                        target = utils.getTargets(args[0]);
                    }
                } else {
                    utils.sendPlayer(player, core.prefix + "<#ff5555>Invalid gamemode.");
                    return;
                }
            } else {
                utils.sendPlayer(player, core.prefix + "<#ff5555>/gm <creative|survival|spectator|adventure> [target]");
                return;
            }
        }
        if (target == null||target.isEmpty()) {
            utils.sendPlayer(player, core.prefix + "<#ff5555>Invalid target.");
            return;
        }
        for (Player p : target) {
            p.setGameMode(mode);
        }
        if (target.size() == 1&&target.contains(player)) {
            utils.sendPlayer(player, core.prefix + "<#55ff55>Set gamemode to " + mode.toString().toLowerCase() + ".");
        } else if (target.size() == 1) {
            for (Player p : target) {
                utils.sendPlayer(player, core.prefix + "<#55ff55>Set " + p.getName() + "'s gamemode to " + mode.toString().toLowerCase() + ".");
            }
        } else {
            utils.sendPlayer(player, core.prefix + "<#55ff55>Set " + target.size() + " players' gamemode to " + mode.toString().toLowerCase() + ".");
        }
    }
}
