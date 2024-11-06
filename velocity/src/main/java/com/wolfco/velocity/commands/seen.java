package com.wolfco.velocity.commands;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import com.wolfco.velocity.utils;
import com.wolfco.velocity.wolfcore;
import com.wolfco.velocity.types.Command;
import com.wolfco.velocity.types.OfflinePlayer;
import net.kyori.adventure.text.Component;

import java.util.UUID;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

public class seen implements Command {
    private final wolfcore plugin;

    public seen(wolfcore server, CommandManager manager) {
        this.plugin = server;
        manager.register(
                manager.metaBuilder("seen").plugin(server).build(), this);
    }

    @Override
    public void onCommand(CommandSource sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Component.text("§6Usage: §e/seen <player>"));
            return;
        }
        Player player = plugin.server.getPlayer(args[0]).orElse(null);
        UUID uuid;
        if (player == null) {
            uuid = plugin.playerManager.checkPlayer(args[0]);
        } else {
            uuid = player.getUniqueId();
        }
        if (uuid == null) {
            sender.sendMessage(Component.text("§6Player §e" + args[0] + "§6 has not joined before."));
            return;
        }
        OfflinePlayer playerData = plugin.playerManager.getOfflinePlayer(uuid);
        if (playerData == null) {
            sender.sendMessage(Component.text("§6Player §e" + args[0] + "§6 has not joined before."));
            return;
        }
        long startTime;
        if (player != null) {
            startTime = playerData.login();
        } else {
            startTime = playerData.logout();
        }
        if (player != null) {
            String servername = player.getCurrentServer().get().getServerInfo().getName();
            Component text = Component.text("§6" + plugin.displayname(player) + ":\n"
                    + "§6Server: §e" + servername + "\n"
                    + "§6Online For: §e" + utils.time(startTime));
            if (sender.hasPermission("wolfcore.seen.ip")) {
                text = text.append(Component.text("\n§6IP: §e" + playerData.ipaddress));
            }
            sender.sendMessage(text);
            return;
        } else {
            utils.getOfflineDisplay(uuid, plugin).thenAcceptAsync(name -> {
            Component text = Component.text("§6" + name  + ":\n"
                    + "§6Last Seen: §e" + utils.time(startTime) + " ago");
            if (sender.hasPermission("wolfcore.seen.ip")) {
                text = text.append(Component.text("\n§6IP: §e" + playerData.ipaddress));
            }
            sender.sendMessage(text);
            });
            return;
        }

    }

    @Override
    public List<String> suggest(final Invocation invocation) {
        if (invocation.arguments().length == 0) {
            Set<String> set = plugin.playerManager.offlineData.getRoutesAsStrings(false);
            List<String> suggestions = new ArrayList<>(set);
            set.forEach((key) -> {
                suggestions.add(key);
            });
            return suggestions;
        }
        String incompleteString = invocation.arguments()[invocation.arguments().length - 1];
        List<String> suggestions = new ArrayList<>();
        plugin.playerManager.offlineData.getRoutesAsStrings(false).forEach((key) -> {
            if (key.startsWith(incompleteString)) {
                suggestions.add(key);
            }
        });
        return suggestions;
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("wolfcore.seen");
    }
}
