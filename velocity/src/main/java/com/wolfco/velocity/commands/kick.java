package com.wolfco.velocity.commands;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.wolfco.velocity.events.punishManager;
import com.wolfco.velocity.types.Command;
import com.wolfco.velocity.wolfcore;

import net.kyori.adventure.text.Component;

public class kick implements Command {
    private final wolfcore plugin;

    public kick(wolfcore server, CommandManager manager) {
        this.plugin = server;
        manager.register(
                manager.metaBuilder("kick").plugin(server).build(), this);
    }

    @Override
    public void onCommand(CommandSource sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Component.text("§6Usage: §e/kick <player> <reason>"));
            return;
        }
        final StringBuilder reason = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            String current = args[i];
            if (i > 0) {
                reason.append(current).append(" ");
            } else {
                continue;
            }
        }
        if (reason.toString().replace(" ", "").length() == 0) {
            sender.sendMessage(Component.text("§6Usage: §e/kick <player> <reason>"));
            return;
        }
        punishManager punishManager = plugin.playerManager.punishManager;
        String code = punishManager.createID();
        plugin.server.getPlayer(args[0]).ifPresentOrElse((player) -> {
            player.disconnect(Component.text("§6You have been kicked from the server.\n§6Reason: §e" + reason.toString()));
            if (sender instanceof Player) {
                punishManager.logPunishment(player.getUniqueId(), ((Player) sender).getUniqueId(),
                        reason.toString(), "kick", (long) 0, code);
            } else {
                punishManager.logPunishment(player.getUniqueId(), "console", reason.toString(), "kick",
                        (long) 0, code);
            }
            sender.sendMessage(
                    Component.text("§6Player §e" + plugin.displayname(player) + "§6 has been kicked. Punishment has been logged."));
        }, () -> {
            sender.sendMessage(Component.text("§6Player §e" + args[0] + "§6 is not online."));
        });
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("wolfcore.seen");
    }
}
