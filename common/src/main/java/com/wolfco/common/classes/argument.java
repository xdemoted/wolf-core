package com.wolfco.common.classes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;

public class Argument implements ArgumentInterface{
    private ArgumentType type;
    private Boolean required;
    private String name;

    public Argument(ArgumentType type, Boolean required) {
        this.type = type;
        this.required = required;
        this.name = type.toString();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Boolean isRequired() {
        return required;
    }

    public ArgumentType getType() {
        return type;
    }

    public List<String> getOptions(CorePlugin core, CommandSender sender, // TODO - Add Exclusivity and * notation
            org.bukkit.command.Command bukkitCommand, String[] args) {
        switch (type) {
            case EXCLUSIVEPLAYER:
            case PLAYER:
                return core.getServer().getOnlinePlayers().stream().map(player -> player.getName()).toList();
            case EXCLUSIVEOTHERPLAYER:
            case OTHERPLAYER:
                List<String> players = new ArrayList<>();

                for (String player : Bukkit.getOnlinePlayers().stream().map(player -> player.getName()).toList()) {
                    if (!player.equals(sender.getName())) {
                        players.add(player);
                    }
                }

                return players;
            case GAMEMODE:
                GameMode[] gameModes = GameMode.values();
                List<String> gameModeNames = new ArrayList<>();

                for (GameMode gameMode : gameModes) {
                    gameModeNames.add(gameMode.toString());
                }

                return gameModeNames;
            case CUSTOM:
                return List.of("CustomArgWithNoOptions");
            default:
                return List.of();
        }
    }

    public Object getValue(CorePlugin core, CommandSender sender, org.bukkit.command.Command bukkitCommand, String searchValue) {
        switch (type) {
            case PLAYER:
                return core.getServer().getPlayer(searchValue);
            case OTHERPLAYER:
                return core.getServer().getPlayer(searchValue);
            case GAMEMODE:
                return GameMode.valueOf(searchValue.toUpperCase());
            default:
                return null;
        }
        
    }

    public char getCode() {
        return type.toString().charAt(0);
    }
}
