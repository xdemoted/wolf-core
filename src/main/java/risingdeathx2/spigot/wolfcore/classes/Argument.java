package risingdeathx2.spigot.wolfcore.classes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import risingdeathx2.spigot.wolfcore.core;

public class Argument {
    public String name;
    public ArgumentType type;
    public Boolean optional;
    public Argument(String name, ArgumentType type, Boolean optional) {
        this.name = name;
        this.type = type;
        this.optional = optional;
    }
    public static Argument player(boolean optional) {
        return new Argument("player", ArgumentType.PLAYER, optional);
    }
    public Object resolve(core core,String arg) {
        if (type == ArgumentType.PLAYER) {
            getPlayer(core, arg);
        } else if (type == ArgumentType.GAMEMODE) {
            switch (arg.toLowerCase()) {
                case "0":
                case "survival":
                    return GameMode.SURVIVAL;
                case "1":
                case "creative":
                    return GameMode.CREATIVE;
                case "2":
                case "adventure":
                    return GameMode.ADVENTURE;
                case "3":
                case "spectator":
                    return GameMode.SPECTATOR;
            }
        }
        return null;
        }
        
        public List<Player> getPlayer(core core, String arg) {
            return getPlayer(core, arg, true);
        }
        public Player getExclusivePlayer(core core, String arg) {
            List<Player> players = getPlayer(core, arg, true);
            if (players.size() == 1) {
                return players.get(0);
            }
            return null;
        }
        public List<Player> getPlayer(core core, String arg, Boolean multiple) {
        if (arg.length() > 3) {
            @SuppressWarnings("unchecked")
            Collection<Player> players = (Collection<Player>) core.getServer().getOnlinePlayers();
            for (Player player : players) {
                if (player.getName().toLowerCase().startsWith(arg.toLowerCase())) {
                    return List.of(player);
                }
            }
        } else if (arg == "*"&& multiple) {
            List<Player> players = new ArrayList<>();
            for (Player player : core.getServer().getOnlinePlayers()) {
                players.add(player);
            }
            return players;
        }
        return null;
    }
    public GameMode getGamemode(core core, String arg) {
        switch (arg.toLowerCase()) {
            case "0":
            case "survival":
                return GameMode.SURVIVAL;
            case "1":
            case "creative":
                return GameMode.CREATIVE;
            case "2":
            case "adventure":
                return GameMode.ADVENTURE;
            case "3":
            case "spectator":
                return GameMode.SPECTATOR;
        }
        return null;
    }
    public Warp getWarp(core core, String arg) {
        if (core.warps.contains(arg)) {
            double X = core.warps.getDouble(arg + ".x");
            double Y = core.warps.getDouble(arg + ".y");
            double Z = core.warps.getDouble(arg + ".z");
            String world = core.warps.getString(arg + ".world");
            if (X != 0 && Y != 0 && Z != 0 && world != null) {
                Warp warp = new Warp();
                warp.x = X;
                warp.y = Y;
                warp.z = Z;
                warp.world = UUID.fromString(world);
                return warp;
            }
        }
        return null;
    }
}
