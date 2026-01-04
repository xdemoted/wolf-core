package com.wolfco.common.commands.arguments;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;

import com.wolfco.common.Utilities;
import com.wolfco.common.classes.ArgumentInterface;
import com.wolfco.common.classes.CorePlugin;

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
        CorePlugin core = CorePlugin.get();
        CommandSender sender = commandStack.getSender();
        List<String> players = core.getServer().getOnlinePlayers().stream().map(p -> p.getName())
                .collect(Collectors.toList());

        if (!self) {
            players.remove(sender.getName());
        }

        return players;
    }

    @Override
    public Object getValue(CommandSourceStack commandStack, String searchValue) {
        return Utilities.getUUIDFromName(name);
    }
}
