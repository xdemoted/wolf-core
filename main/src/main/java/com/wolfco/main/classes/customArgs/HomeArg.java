package com.wolfco.main.classes.customArgs;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.ArgumentInterface;
import com.wolfco.common.classes.ArgumentType;
import com.wolfco.common.classes.CorePlugin;
import com.wolfco.main.core;
import com.wolfco.main.classes.PlayerData;

public class HomeArg implements ArgumentInterface {
    public boolean required = true;

    public HomeArg(boolean required) {
        
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
    public String getName() {
        return "HOME";
    }

    @Override
    public List<String> getOptions(CorePlugin core, CommandSender sender,
            org.bukkit.command.Command bukkitCommand, String[] args) {
        core plugin = (core) core;
        
        if (!(sender instanceof Player)) List.of();

        PlayerData playerData = plugin.playerManager.getPlayerData((Player) sender);

        return playerData.homes.keySet().stream().toList();
    }

    @Override
    public Object getValue(CorePlugin core, CommandSender sender, org.bukkit.command.Command bukkitCommand,
            String searchValue) {
        core plugin = (core) core;
        
        if (!(sender instanceof Player)) return null;

        PlayerData playerData = plugin.playerManager.getPlayerData((Player) sender);

        return playerData.homes.get(searchValue);
    }
}
