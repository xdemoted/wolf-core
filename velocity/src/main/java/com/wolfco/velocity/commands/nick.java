package com.wolfco.velocity.commands;

import java.util.Collection;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import com.wolfco.velocity.utils;
import com.wolfco.velocity.wolfcore;
import com.wolfco.velocity.types.Command;
import com.wolfco.velocity.types.PlayerData;
import net.kyori.adventure.text.Component;

public class nick implements Command {
    private final wolfcore plugin;

    public nick(wolfcore server, CommandManager manager) {
        this.plugin = server;
        manager.register(
                manager.metaBuilder("nick").plugin(server).build(), this);
    }

    @Override
    public void onCommand(CommandSource sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(Component.text("§6Usage: §e/nick [player] <nickname>"));
            return;
        }
        Player player = plugin.server.getPlayer(args[0]).orElse(null);
        Collection<String> argslist = utils.convertArray(args);
        PlayerData playerData;
        if (player != null) {
            argslist.remove(args[0]);
            playerData = plugin.playerManager.getPlayerData(player);
        } else if (player == null&&sender instanceof Player) {
            playerData = plugin.playerManager.getPlayerData((Player) sender);
        } else {
            sender.sendMessage(Component.text("§6You must specify a player."));
            return;
        }
        String nickname = utils.joinCollection(argslist, " ");
        int limit = plugin.config.getInt("MaxNickLength", 16);
        if (nickname.length() > limit) {
            sender.sendMessage(Component.text("§6Nickname is too long. Max length is " + limit + " characters."));
            return;
        } else if (!nickname.matches("^[a-zA-Z0-9]+$")) {
            sender.sendMessage(Component.text("§6Nickname must be alphanumeric."));
            return;
        } else {
            playerData.setNickname(nickname);
            playerData.save();
            if (player != null) {
                player.sendMessage(Component.text("§6Your nickname has been set to §e" + nickname));
            } else {
                sender.sendMessage(Component.text("§6Player §e" + args[0] + "§6's nickname has been set to §e" + nickname));
            }
        }

    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("wolfcore.seen");
    }
}
