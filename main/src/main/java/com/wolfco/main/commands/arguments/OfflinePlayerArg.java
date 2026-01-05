package com.wolfco.main.commands.arguments;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;

import com.wolfco.common.classes.ArgumentInterface;
import com.wolfco.main.Core;
import com.wolfco.main.player.profiles.ProfileManager;

import io.papermc.paper.command.brigadier.CommandSourceStack;

public class OfflinePlayerArg implements ArgumentInterface {
    private final ProfileManager profileManager = Core.get().getScope().get(ProfileManager.class);
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
        List<String> players = profileManager.getAllProfiles().stream()
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
        List<ProfileManager.reducedPlayerInfo> players = profileManager.getAllProfiles();

        for (ProfileManager.reducedPlayerInfo p : players) {
            if (p.name.equalsIgnoreCase(searchValue)) {
                return profileManager.loadOfflineProfile(p.uuid);
            }
        }

        throw error("Argument %s requires a valid offline player.", name);
    }

    private String node = null;

    @Override
    public ArgumentInterface setNode(String node) {
        this.node = node;
        return this;
    }

    @Override
    public String getNode() {
        return this.node;
    }
}
