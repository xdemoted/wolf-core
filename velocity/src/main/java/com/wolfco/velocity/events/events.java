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
import com.wolfco.velocity.JDA.JDAListener;
import com.wolfco.velocity.modules.tablist;
import com.wolfco.velocity.wolfcore;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

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
            String formatting = in.readUTF();
            String message = in.readUTF();
            Boolean color = in.readBoolean();
            plugin.broadcast(MiniMessage.miniMessage().deserialize(formatting,
                    (color ? Placeholder.parsed("message", message) : Placeholder.parsed("message", message))));
        }
    }

    @Subscribe
    public void onLogout(DisconnectEvent event) {
        Player player = event.getPlayer();
        try {
            jda.sendMessage(player.getUsername(), ":arrow_left: **Has left the network.**",
                    "https://crafthead.net/helm/" + player.getUniqueId());
        } catch (MalformedURLException e) {
            plugin.logger.warn(e.getMessage());
        } catch (IOException e) {
            plugin.logger.warn(e.getMessage());
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
                    plugin.logger.warn(e.getMessage());
                }
                player.sendMessage(
                        Component.text("§8[§aWelcome§8]§e There is currently §a" + plugin.server.getPlayerCount()
                                + "§e players online.\nRun /list to get a full list of players."));
                plugin.server.getAllPlayers().forEach(p -> {
                    if (!p.getUsername().equals(player.getUsername()))
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
                plugin.logger.warn(e.getMessage());
            }
        } else {
            plugin.logger.warn("No current server found.");
        }
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
