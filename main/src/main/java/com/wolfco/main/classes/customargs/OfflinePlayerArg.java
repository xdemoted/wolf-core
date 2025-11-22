package com.wolfco.main.classes.customargs;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.wolfco.common.classes.ArgumentInterface;
import com.wolfco.common.classes.CorePlugin;
import com.wolfco.main.Core;
import com.wolfco.main.events.PlayerManager;

public class OfflinePlayerArg implements ArgumentInterface {
    final boolean required;
    boolean self = false;
    String name = "PLAYER";


    public OfflinePlayerArg(boolean required) {
        this.required = required;
    }

    public OfflinePlayerArg includeSender(boolean self) {
        this.self = self;
        return this;
    }

    @Override
    public Boolean isRequired() {
        return required;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ArgumentInterface setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public List<String> getOptions(CorePlugin core, CommandSender sender, Command bukkitCommand, String[] args) {
        List<String> players = PlayerManager.getAllPlayerDataDocuments().stream()
                .map(p -> p.name)
                .collect(Collectors.toList());

        if (!self) {
            players.remove(sender.getName());
        }

        return players;
    }

    @Override
    public Object getValue(CorePlugin core, CommandSender sender, Command bukkitCommand, String searchValue) {
        List<PlayerManager.reducedPlayerInfo> allPlayers = PlayerManager.getAllPlayerDataDocuments();

        for (PlayerManager.reducedPlayerInfo p : allPlayers) {
            if (p.name.equalsIgnoreCase(searchValue)) {
                return ((Core) Core.get()).getPlayerManager().getOfflinePlayer(p.uuid);
            }
        }

        throw error("Argument %s requires a valid offline player.",name);
    }
    
}
