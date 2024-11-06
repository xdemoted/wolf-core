package com.wolfco.skyblock.classes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import com.wolfco.skyblock.main;

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
    public Object resolve(main main,String arg) {
        if (type == ArgumentType.PLAYER) {
            getPlayer(main, arg);
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
        
        public List<Player> getPlayer(main main, String arg) {
            return getPlayer(main, arg, true);
        }
        public Player getExclusivePlayer(main main, String arg) {
            List<Player> players = getPlayer(main, arg, true);
            if (players.size() == 1) {
                return players.get(0);
            }
            return null;
        }
        public List<Player> getPlayer(main main, String arg, Boolean multiple) {
        if (arg.length() > 3) {
            @SuppressWarnings("unchecked")
            Collection<Player> players = (Collection<Player>) main.getServer().getOnlinePlayers();
            for (Player player : players) {
                if (player.getName().toLowerCase().startsWith(arg.toLowerCase())) {
                    return List.of(player);
                }
            }
        } else if (arg == "*"&& multiple) {
            List<Player> players = new ArrayList<>();
            for (Player player : main.getServer().getOnlinePlayers()) {
                players.add(player);
            }
            return players;
        }
        return null;
    }
    public GameMode getGamemode(main main, String arg) {
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
}