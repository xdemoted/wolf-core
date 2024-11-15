package com.wolfco.velocity.commands;

import java.util.Collection;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.wolfco.velocity.types.Command;
import com.wolfco.velocity.wolfcore;

import net.kyori.adventure.text.Component;

public class list implements Command {
    private final wolfcore plugin;

    public list(wolfcore server, CommandManager manager) {
        this.plugin = server;
        manager.register(
                manager.metaBuilder("list").plugin(server).build(), this);
    }

    @Override
    public void onCommand(CommandSource sender, String[] args) {
        if (args.length > 0) {
            sender.sendMessage(Component.text("§6Usage: §e/list"));
            return;
        }
        plugin.server.getAllServers().forEach((server) -> {
            Collection<Player> players = server.getPlayersConnected();
            if (!players.isEmpty()) {
                sender.sendMessage(Component.text("§6" + server.getServerInfo().getName()));
                players.forEach((player) -> {
                    sender.sendMessage(Component.text("§6 - §e" + plugin.displayname(player)));
                });
            }
        });
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("wolfcore.seen");
    }
}
