package com.wolfco.main.classes.customargs;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;

import com.wolfco.common.classes.ArgumentInterface;
import com.wolfco.main.Core;
import com.wolfco.main.events.PlayerManager;

import io.papermc.paper.command.brigadier.CommandSourceStack;

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
    public List<String> getOptions(CommandSourceStack commandStack, String[] args) {
        List<String> players = PlayerManager.getAllPlayerDataDocuments().stream()
                .map(p -> p.name)
                .collect(Collectors.toList());
        CommandSender sender = commandStack.getSender();

        if (!self) {
            players.remove(sender.getName());
        }

        return players;
    }

    @Override
    public Object getValue(CommandSourceStack commandStack, String searchValue) {
        List<PlayerManager.reducedPlayerInfo> allPlayers = PlayerManager.getAllPlayerDataDocuments();

        for (PlayerManager.reducedPlayerInfo p : allPlayers) {
            if (p.name.equalsIgnoreCase(searchValue)) {
                return ((Core) Core.get()).getPlayerManager().getOfflinePlayer(p.uuid);
            }
        }

        throw error("Argument %s requires a valid offline player.",name);
    }
    
}
