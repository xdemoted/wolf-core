package com.wolfco.main.commands.arguments;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.ArgumentInterface;
import com.wolfco.main.Core;
import com.wolfco.main.player.profiles.ProfileManager;
import com.wolfco.main.player.profiles.classes.Profile;

import io.papermc.paper.command.brigadier.CommandSourceStack;

public class HomeArgument implements ArgumentInterface {
    private final ProfileManager profileManager = Core.get().getScope().get(ProfileManager.class);

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
    public List<String> getOptions(CommandSourceStack commandStack, String[] args) {
        CommandSender sender = commandStack.getSender();

        if (!(sender instanceof Player))
            List.of();

        Profile profile = profileManager.getCachedProfile((Player) sender);

        return profile.homes.keySet().stream().toList();
    }

    @Override
    public Object getValue(CommandSourceStack commandStack,
            String searchValue) {
        CommandSender sender = commandStack.getSender();

        if (!(sender instanceof Player))
            return null;

        Profile profile = profileManager.getCachedProfile((Player) sender);

        return profile.homes.get(searchValue);
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
