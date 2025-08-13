package com.wolfco.main.classes.customargs;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.ArgumentInterface;
import com.wolfco.common.classes.CorePlugin;
import com.wolfco.main.Core;
import com.wolfco.main.classes.mongoDB.subtypes.NamedLocation;

public class HomeArgument implements ArgumentInterface {
    private boolean required = true;
    private String name = "HOME";

    public HomeArgument(boolean required) {
        this.required = required;
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

        if (!(sender instanceof Player))
            return List.of();
 
        return plugin.getPlayerManager().getHomes((Player) sender);
    }

    @Override
    public Object getValue(CorePlugin core, CommandSender sender, org.bukkit.command.Command bukkitCommand,
            String searchValue) {
        Core plugin = (Core) core;

        if (!(sender instanceof Player)) {
            return null;
        }

        CompletableFuture<NamedLocation> home = new CompletableFuture<>();

        plugin.getRedisManager().getPlayerAsync(((Player) sender).getUniqueId().toString()).thenAccept(playerData -> {
            String[] searchValueInner = {searchValue.toLowerCase()};

            if (playerData == null) {
                plugin.sendPreset(sender, "generic.invaliddata");
                return;
            }

            if ("".equals(searchValueInner[0])) {
                searchValueInner[0] = "home";
            }

            playerData.getHomes().forEach(location -> {
                if (location.getName().equalsIgnoreCase(searchValueInner[0])) {
                    home.complete(location);
                }
            });

            if (!home.isDone()) {
                home.complete(null);
            }
        });

        return home;
    }
}
