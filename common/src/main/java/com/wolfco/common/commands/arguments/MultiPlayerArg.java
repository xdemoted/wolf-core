package com.wolfco.common.commands.arguments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.ArgumentInterface;
import com.wolfco.common.classes.CorePlugin;

import io.papermc.paper.command.brigadier.CommandSourceStack;

public class MultiPlayerArg implements ArgumentInterface {

    final boolean required;
    boolean self = true;
    String name = "PLAYER";

    public MultiPlayerArg(boolean required) {
        this.required = required;
    }

    public MultiPlayerArg includeSender(boolean self) {
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
    public MultiPlayerArg setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public List<String> getOptions(CommandSourceStack commandStack, String[] args) {
        CorePlugin core = CorePlugin.get();
        CommandSender sender = commandStack.getSender();
        List<String> players = core.getServer().getOnlinePlayers().stream().map(p -> p.getName()).collect(Collectors.toList());
        
        players.add("*");

        if (!self) {
            players.remove(sender.getName());
        }

        return players;
    }

    @Override
    public Collection<? extends Player> getValue(CommandSourceStack commandStack, String searchValue) {
        List<Player> players;
        CorePlugin core = CorePlugin.get();
        CommandSender sender = commandStack.getSender();

        if (searchValue.equals("*")) {
            players = new ArrayList<>(core.getServer().getOnlinePlayers());
        } else {
            players = new ArrayList<>();
            players.add(core.getServer().getPlayer(searchValue));
        }

        if (sender instanceof Player && !self) {
            players.remove((Player) sender);

            if (players.isEmpty()) {
                throw error("Argument %s cannot target yourself", name);
            }
        } else if (players.size() == 1 && players.iterator().next() == null) {
            throw error("Argument %s: Player %s not found", name, searchValue);
        }

        return players;
    }

}
