package com.wolfco.main.classes.customArgs;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.ArgumentInterface;
import com.wolfco.common.classes.ArgumentType;
import com.wolfco.common.classes.CorePlugin;
import com.wolfco.main.Core;
import com.wolfco.main.classes.PlayerData;

public class HomeArgument implements ArgumentInterface {
    private boolean required = true;
    private String name = "HOME";

    public HomeArgument(boolean required) {
        this.required = required;
    }

    @Override
    public ArgumentType getType() {
        return ArgumentType.CUSTOM;
    }

    @Override
    public Boolean isRequired() {
        return required;
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
    public List<String> getOptions(CorePlugin core, CommandSender sender,
            org.bukkit.command.Command bukkitCommand, String[] args) {
        Core plugin = (Core) core;
        
        if (!(sender instanceof Player)) List.of();

        PlayerData playerData = plugin.PlayerManager.getPlayerData((Player) sender);

        return playerData.homes.keySet().stream().toList();
    }

    @Override
    public Object getValue(CorePlugin core, CommandSender sender, org.bukkit.command.Command bukkitCommand,
            String searchValue) {
        Core plugin = (Core) core;
        
        if (!(sender instanceof Player)) return null;

        PlayerData playerData = plugin.PlayerManager.getPlayerData((Player) sender);

        return playerData.homes.get(searchValue);
    }

        @Override
    public String getError() {
                return String.format("Argument {0} requires a valid home.", name);

    }
}
