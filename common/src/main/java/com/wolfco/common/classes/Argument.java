package com.wolfco.common.classes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.Utilities;
import com.wolfco.common.classes.types.ArgumentType;

public class Argument implements ArgumentInterface {

    final ArgumentType type;
    final Boolean required;
    String name;

    public Argument(ArgumentType type, Boolean required) {
        this.type = type;
        this.required = required;
        this.name = switch (type) {
            case PLAYER, EXCLUSIVEOTHERPLAYER, EXCLUSIVEPLAYER, OTHERPLAYER ->
                "PLAYER";
            default ->
                type.toString();
        };
    }

    @Override
    public ArgumentInterface setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Boolean isRequired() {
        return required;
    }

    @Override
    public List<String> getOptions(CorePlugin core, CommandSender sender,
            org.bukkit.command.Command bukkitCommand, String[] args) {
        switch (type) {
            case EXCLUSIVEPLAYER -> {
                return core.getServer().getOnlinePlayers().stream().map(Player::getName).toList();
            }
            case PLAYER -> {
                List<String> players = new ArrayList<>();
                players.add("*");

                players.addAll(core.getServer().getOnlinePlayers().stream().map(Player::getName).toList());

                return players;
            }
            case EXCLUSIVEOTHERPLAYER -> {
                List<String> players = new ArrayList<>();

                for (String player : Bukkit.getOnlinePlayers().stream().map(Player::getName).toList()) {
                    if (!player.equals(sender.getName())) {
                        players.add(player);
                    }
                }

                return players;
            }
            case OTHERPLAYER -> {
                List<String> players = new ArrayList<>();

                for (String player : Bukkit.getOnlinePlayers().stream().map(Player::getName).toList()) {
                    if (!player.equals(sender.getName())) {
                        players.add(player);
                    }
                }

                players.add("*");

                return players;
            }
            case GAMEMODE -> {
                GameMode[] gameModes = GameMode.values();
                List<String> gameModeNames = new ArrayList<>();

                for (GameMode gameMode : gameModes) {
                    gameModeNames.add(gameMode.toString().toLowerCase());
                }

                return gameModeNames;
            }
            case CUSTOM -> {
                return List.of("CustomArgWithNoOptions");
            }
            default -> {
                return List.of();
            }
        }
    }

    @Override
    public Object getValue(CorePlugin core, CommandSender sender, org.bukkit.command.Command bukkitCommand,
            String searchValue) {
        Object returnValue;

        if (sender instanceof Player player && player.getName().equalsIgnoreCase(searchValue)
                && (type == ArgumentType.OTHERPLAYER || type == ArgumentType.EXCLUSIVEOTHERPLAYER)) {

            throw new IllegalArgumentException("Argument " + getName() + " cannot be the same as sender");
        }

        returnValue = switch (type) {
            case STRING ->
                searchValue;
            case ALPHANUMERICSTRING ->
                (searchValue.matches("^[a-zA-Z0-9]*$")) ? searchValue : null;
            case BOOLEAN ->
                (searchValue.equalsIgnoreCase("true") || searchValue.equalsIgnoreCase("false"))
                        ? Boolean.parseBoolean(searchValue)
                        : null;

            case GAMEMODE ->
                GameMode.valueOf(searchValue.toUpperCase());

            case PLAYER -> {
                Collection<Player> players = Utilities.getTargets(core, searchValue);

                if (players.isEmpty()) {
                    throw new IllegalArgumentException("Argument " + getName() + " requires a valid player");
                }

                yield players;
            }
            case EXCLUSIVEPLAYER -> {
                Player player = Utilities.getTarget(core, searchValue);

                if (player == null) {
                    throw new IllegalArgumentException("Argument " + getName() + " requires a valid player");
                }

                yield player;
            }
            case OTHERPLAYER -> {
                Collection<Player> players = Utilities.getTargets(core, searchValue);

                if (sender == null) {
                    yield players;
                }

                if (players.isEmpty()) {
                    throw new IllegalArgumentException("Argument " + getName() + " requires a valid player");
                }

                players.removeIf(player -> player.getName().equalsIgnoreCase(sender.getName()));

                yield players;
            }
            case EXCLUSIVEOTHERPLAYER -> {
                Player player = Utilities.getTarget(core, searchValue);

                if (sender == null) {
                    yield player;
                }

                if (player == null) {
                    throw new IllegalArgumentException("Argument " + getName() + " requires a valid player");
                }

                if (player.getName().equalsIgnoreCase(sender.getName())) {
                    throw new IllegalArgumentException("Argument " + getName() + " cannot be the same as the sender");
                }

                yield player;
            }

            case WORLD ->
                core.getServer().getWorld(searchValue);

            default ->
                null;
        };

        if (isRequired() && returnValue == null) {
            throw new IllegalArgumentException("Argument " + getName() + " is required");
        }

        return returnValue;

    }
}
