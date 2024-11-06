package com.wolfco.velocity.events;

import java.io.IOException;
import java.net.MalformedURLException;

import java.util.Optional;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import com.wolfco.velocity.utils;
import com.wolfco.velocity.wolfcore;
import com.wolfco.velocity.JDA.JDAListener;
import com.wolfco.velocity.modules.tablist;
import net.kyori.adventure.text.Component;

public class events {
    public wolfcore plugin;
    public static final MinecraftChannelIdentifier IDENTIFIER = MinecraftChannelIdentifier.from("core:main");
    public JDAListener jda;
    public Component messageComponent;

    public events(wolfcore plugin, JDAListener jda) {
        this.plugin = plugin;
        this.jda = jda;
        plugin.server.getChannelRegistrar().register(IDENTIFIER);
    }

    @Subscribe
    public void onMessage(PluginMessageEvent event) {
        plugin.logger.info(event.getIdentifier().getId());
        if (event.getIdentifier() != IDENTIFIER)
            return;
        ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
        String subChannel = in.readUTF();
        if (subChannel.equals("globalchat")) {
            String message = in.readUTF();
            plugin.broadcast(utils.parseColors(message));
        }
    }

    @Subscribe
    public void onLogout(DisconnectEvent event) {
        Player player = event.getPlayer();
        try {
            jda.sendMessage(player.getUsername(), ":arrow_left: **Has left the network.**",
                    "https://crafthead.net/helm/" + player.getUniqueId());
        } catch (MalformedURLException e) {
            plugin.logger.warning(e.getMessage());
        } catch (IOException e) {
            plugin.logger.warning(e.getMessage());
        }
        Component text = Component.text("§8[§aNetwork§8]§a " + player.getUsername() + " §eHas left the server.");
        plugin.broadcast(text);
    }
    @Subscribe
    public void onServerSwitch(ServerPostConnectEvent event) {
        Player player = event.getPlayer();
        Optional<ServerConnection> optionalCurrent = player.getCurrentServer();
        if (optionalCurrent.isPresent()) {
            String currentName = player.getCurrentServer().get().getServerInfo().getName();
            RegisteredServer previous = event.getPreviousServer();
            if (previous == null) {
                try {
                    jda.sendMessage(
                            player.getUsername(), ":arrow_right: **Has joined the network. ["
                                    + currentName + "]**",
                            "https://crafthead.net/helm/" + event.getPlayer().getUniqueId());
                } catch (IOException e) {
                    plugin.logger.warning(e.getMessage());
                }
                player.sendMessage(
                        Component.text("§8[§aWelcome§8]§e There is currently §a" + plugin.server.getPlayerCount()
                                + "§e players online.\nRun /list to get a full list of players."));
                plugin.server.getAllPlayers().forEach(p -> {
                    if (p.getUsername() != player.getUsername())
                        p.sendMessage(
                                Component.text(
                                        "§8[§aNetwork§8]§a " + player.getUsername() + " §eHas joined the server."));
                });
                return;
            }
            String previousName = previous.getServerInfo().getName();
            plugin.broadcast(Component.text("§8[§aNetwork§8]§a " + player.getUsername() + " §eswitched to §8[§a"
                    + player.getCurrentServer().get().getServerInfo().getName() + "§8]"));
            try {
                jda.sendMessage(player.getUsername(),
                        "[" + previousName + "] :arrow_right: ["
                                + currentName + "]",
                        "https://crafthead.net/helm/" + event.getPlayer().getUniqueId());
            } catch (IOException e) {
                plugin.logger.warning(e.getMessage());
            }
        } else {
            plugin.logger.warning("No current server found.");
        }
    }

    int[] toRGB(String hex) {
        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);
        return new int[] { r, g, b };
    }

    String toHex(int[] rgb) {
        return String.format("%02x%02x%02x", rgb[0], rgb[1], rgb[2]);
    }
    @Subscribe
    public void connect(ServerConnectedEvent event) {
        tablist.update(plugin);
    }
    @Subscribe
    public void disconnect(DisconnectEvent event) {
        tablist.update(plugin);
    }
}
