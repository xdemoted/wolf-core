package com.wolfco.velocity.events;

import java.util.ArrayList;
import java.util.List;
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
import com.wolfco.velocity.managers.WebhookManager;
import com.wolfco.velocity.modules.tablist;
import com.wolfco.velocity.wolfcore;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;

public class events {

    public wolfcore plugin;
    public static final MinecraftChannelIdentifier IDENTIFIER = MinecraftChannelIdentifier.from("core:main");
    public Component messageComponent;

    public List<Player> afkPlayers = new ArrayList<>();

    public MiniMessage serializer;

    public events(wolfcore plugin) {
        this.plugin = plugin;
        plugin.server.getChannelRegistrar().register(IDENTIFIER);
        serializer = MiniMessage.builder()
        .tags(TagResolver.builder()
                .resolver(StandardTags.color())
                .resolver(StandardTags.decorations())
                .build()
        )
        .build();
    }

    @Subscribe
    public void onMessage(PluginMessageEvent event) {
        plugin.logger.info(event.getIdentifier().getId());
        if (event.getIdentifier() != IDENTIFIER) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
        String subChannel = in.readUTF();
        switch (subChannel) {
            case "globalchat" -> {
                String playerName = in.readUTF();
                String formatting = in.readUTF();
                String message = in.readUTF();
                Boolean color = in.readBoolean();

                String[] formattingParts = formatting.split("<message>", 2);

                if (formattingParts.length != 2) {
                    plugin.logger.warn("Invalid formatting string received: " + formatting);
                    return;
                }

                plugin.logger.info(formattingParts[0] + message + formattingParts[1]);

                Component preMessage = MiniMessage.miniMessage().deserialize(formattingParts[0] + "<reset>");
                Component postMessage = MiniMessage.miniMessage().deserialize(formattingParts[1]);
                Component fullMessage = preMessage.append(color ? serializer.deserialize(message) : Component.text(message)).append(postMessage);

                plugin.broadcast(fullMessage);
                Player player = plugin.server.getPlayer(playerName).orElse(null);
                
                if (player != null) {
                    WebhookManager.sendMessage(player, message);
                }
            }
            case "broadcast" -> {
                String message = in.readUTF();
                plugin.broadcast(MiniMessage.miniMessage().deserialize(message));
            }
            case "afk" -> {
                String playerName = in.readUTF();
                Boolean afk = in.readBoolean();
                Player player = plugin.server.getPlayer(playerName).orElse(null);

                if (player != null) {
                    if (afk && !afkPlayers.contains(player)) {
                        afkPlayers.add(player);
                    } else if (!afkPlayers.contains(player)) {
                        afkPlayers.remove(player);
                    }
                    tablist.update(plugin);
                }
            }
            default -> {
            }
        }
    }

    @Subscribe
    public void onLogout(DisconnectEvent event) {
        Player player = event.getPlayer();
        WebhookManager.sendMessage(player, "## :arrow_left: **Has left the network.**", false);
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
                WebhookManager.sendMessage(player, "## :arrow_right: **Has joined the network. ["
                        + currentName + "]**", false);
                player.sendMessage(
                        Component.text("§8[§aWelcome§8]§e There is currently §a" + plugin.server.getPlayerCount()
                                + "§e players online.\nRun /list to get a full list of players."));
                plugin.server.getAllPlayers().forEach(p -> {
                    if (!p.getUsername().equals(player.getUsername())) {
                        p.sendMessage(
                                Component.text(
                                        "§8[§aNetwork§8]§a " + player.getUsername() + " §eHas joined the server."));
                    }
                });
                return;
            }
            String previousName = previous.getServerInfo().getName();
            plugin.broadcast(Component.text("§8[§aNetwork§8]§a " + player.getUsername() + " §eswitched to §8[§a"
                    + player.getCurrentServer().get().getServerInfo().getName() + "§8]"));
            WebhookManager.sendMessage(player, "## [" + previousName + "] :arrow_right: ["
                    + currentName + "]", false);
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
