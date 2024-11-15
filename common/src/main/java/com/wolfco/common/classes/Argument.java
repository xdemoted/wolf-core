package com.wolfco.common.classes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.Utilities;

public class Argument implements ArgumentInterface {

    private final ArgumentType type;
    private final Boolean required;
    private String name;

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
    public ArgumentType getType() {
        return type;
    }

    @Override
    public List<String> getOptions(CorePlugin core, CommandSender sender, // TODO - Add Exclusivity and * notation
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

                for (String player : Bukkit.getOnlinePlayers().stream().map(player -> player.getName()).toList()) {
                    if (!player.equals(sender.getName())) {
                        players.add(player);
                    }
                }

                return players;
            }
            case OTHERPLAYER -> {
                List<String> players = new ArrayList<>();

                for (String player : Bukkit.getOnlinePlayers().stream().map(player -> player.getName()).toList()) {
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
                    gameModeNames.add(gameMode.toString());
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
    public Object getValue(CorePlugin core, CommandSender sender, org.bukkit.command.Command bukkitCommand, String searchValue) {
        Object returnValue;

        if (sender instanceof Player player) {
            if (type == ArgumentType.OTHERPLAYER || type == ArgumentType.EXCLUSIVEOTHERPLAYER) {
                if (player.getName().equalsIgnoreCase(searchValue)) {
                    Utilities.sendColorText(core.getAdventure().sender(sender), core.getMessage("generic.self"));
                    throw new IllegalArgumentException("Argument " + getName() + " cannot be the same as the sender");
                }
            }
        }

        returnValue = switch (type) {
            case STRING ->
                searchValue;
            case ALPHANUMERICSTRING ->
                (searchValue.matches("^[a-zA-Z0-9]*$")) ? searchValue : null;
            case DOUBLE ->
                (searchValue.matches("^[0-9]*$")) ? Double.parseDouble(searchValue) : null;
            case INTEGER ->
                (searchValue.matches("^[0-9]*$")) ? Integer.parseInt(searchValue) : null;
            case BOOLEAN ->
                (searchValue.equalsIgnoreCase("true") || searchValue.equalsIgnoreCase("false")) ? Boolean.parseBoolean(searchValue) : null;

            case GAMEMODE ->
                GameMode.valueOf(searchValue.toUpperCase());

            case PLAYER ->
                Utilities.getTargets(core, name);
            case EXCLUSIVEPLAYER ->
                Utilities.getTarget(core, name);
            case OTHERPLAYER ->
                Utilities.getTargets(core, name);
            case EXCLUSIVEOTHERPLAYER -> {
                Collection<Player> players = Utilities.getTargets(core, name);

                if (sender == null) {
                    yield players;
                }

                players.removeIf(player -> player.getName().equalsIgnoreCase(sender.getName()));

                yield players;
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

    @Override
    public String getError() {
        return String.format("Argument %1s %2s", name, switch (type) {
            case STRING ->
                "requires a valid string.";
            case ALPHANUMERICSTRING ->
                "requires a valid alphanumeric string.";
            case DOUBLE ->
                "requires a valid number.";
            case INTEGER ->
                "requires a valid integer.";
            case BOOLEAN ->
                "requires true or false.";
            case GAMEMODE ->
                "requires a valid gamemode.";
            case PLAYER, EXCLUSIVEPLAYER, OTHERPLAYER, EXCLUSIVEOTHERPLAYER ->
                "expected a valid player.";
            case WORLD ->
                "requires a valid world.";
            case SUBCOMMAND ->
                "requires a valid subcommand.";
            default ->
                "Invalid argument implementation.";
        });
    }
}
