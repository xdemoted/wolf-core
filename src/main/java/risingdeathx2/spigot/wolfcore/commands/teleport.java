package risingdeathx2.spigot.wolfcore.commands;

import org.bukkit.entity.Player;
import java.util.Collection;

import risingdeathx2.spigot.wolfcore.core;
import risingdeathx2.spigot.wolfcore.utils;

public class teleport {
    public core core;
    public Player player;
    public String alias;
    public String[] args;
    @SuppressWarnings("static-access")
    public teleport(core core, Player player, String alias, String[] args) {
        utils utils = new utils(core);
        this.core = core;
        this.player = player;
        this.alias = alias;
        this.args = args;
        // Alias Management
        if (!(alias.equalsIgnoreCase("teleport") || alias.equalsIgnoreCase("tp"))) {
            runAlias();
            return;
        }
        if (args.length == 1) {
            Player target = utils.getTarget(args[0]);
            if (target != null) {
                player.teleport(target);
                utils.sendPlayer(player, core.prefix + "Teleported to " + target.getName());
            } else {
                utils.sendPlayer(player, core.prefix + "<#ff0000>Player not found");
            }
            return;
        } else if (args.length == 2) {
            Collection<Player> target = utils.getTargets(args[0]);
            Player target2 = utils.getTarget(args[1]);
            if (target != null && !target.isEmpty() && target2 != null) {
                for (Player p : target) {
                    p.teleport(target2);
                }
                if ((target.size() - 1) > 1) {
                    utils.sendPlayer(player,
                            core.prefix + "Teleported " + (target.size() - 1) + " players to " + target2.getName());
                } else {
                    utils.sendPlayer(player, core.prefix + "Teleported " + target.iterator().next().getName()
                            + " to " + target2.getName());
                }
            } else {
                utils.sendPlayer(player, core.prefix + "<#ff0000>Player not found");
            }
            return;
        }
        utils.sendPlayer(player, core.prefix + "<#ff0000>Usage:<#ff5555> /" + alias + " <player> [player]");
    }
    
    @SuppressWarnings("static-access")
    public void runAlias() {
        utils utils = new utils(core);
        if (alias.equalsIgnoreCase("tpall")) {
            if (args.length == 1) {
                Player target = utils.getTarget(args[0]);
                if (target != null) {
                    for (Player p : player.getServer().getOnlinePlayers()) {
                        p.teleport(target);
                    }
                    utils.sendPlayer(player, core.prefix + "Teleported " + (player.getServer().getOnlinePlayers().size() - 1) + " players to you");
                } else {
                    utils.sendPlayer(player, core.prefix + "<#ff0000>Player not found");
                }
                return;
            } else {
                for (Player p : player.getServer().getOnlinePlayers()) {
                    p.teleport(player);
                }
                utils.sendPlayer(player, core.prefix + "Teleported all players to you");
                return;
            }
        } else if (alias.equalsIgnoreCase("tphere")) {
            if (args.length == 1) {
                Player target = utils.getTarget(args[0]);
                if (target != null) {
                    target.teleport(player);
                    utils.sendPlayer(player, core.prefix + "Teleported " + target.getName() + " to you");
                } else {
                    utils.sendPlayer(player, core.prefix + "<#ff0000>Player not found");
                }
                return;
            } else {
                utils.sendPlayer(player, core.prefix + "<#ff0000>Usage:<#ff5555> /" + alias + " <player>");
                return;
            }
        }
    }
}
