package com.wolfco.common.classes.argumenthandlers;

import java.util.Collection;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.ArgumentInterface;
import com.wolfco.common.classes.CorePlugin;

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
    public List<String> getOptions(CorePlugin core, CommandSender sender, Command bukkitCommand, String[] args) {
        Collection<String> players = core.getServer().getOnlinePlayers().stream().map(p -> p.getName()).toList();
        players.add("*");

        if (!self) {
            players.remove(sender.getName());
        }

        return (List<String>) players;
    }

    @Override
    public Collection<? extends Player> getValue(CorePlugin core, CommandSender sender, Command bukkitCommand, String searchValue) {
        Collection<? extends Player> players;

        if (searchValue.equals("*")) {
            players = core.getServer().getOnlinePlayers();
        } else {
            players = List.of(core.getServer().getPlayer(searchValue));
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
