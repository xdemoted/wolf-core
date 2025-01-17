package com.wolfco.velocity.events;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.wolfco.velocity.types.OfflinePlayer;
import com.wolfco.velocity.types.PlayerData;
import com.wolfco.velocity.types.Punishment;
import com.wolfco.velocity.wolfcore;

import dev.dejvokep.boostedyaml.YamlDocument;
import net.kyori.adventure.text.Component;

public final class playerManager {

    public Map<UUID, PlayerData> players = new HashMap<>();
    public wolfcore core;
    public YamlDocument offlineData;
    public YamlDocument punishmentData;

    public playerManager(wolfcore core) {
        this.core = core;
        Collection<Player> onlinePlayers = core.server.getAllPlayers();
        offlineData = core.getConfig("playerHistory.yml", core.dataDirectory);
        punishmentData = core.getConfig("punishments.yml", core.dataDirectory);

        if (!onlinePlayers.isEmpty()) {
            for (Player player : onlinePlayers) {
                onJoin(player);
                checkPlayer(player);
            }
            registerEvents();
        }
    }

    private void registerEvents() {
        core.server.getEventManager().register(core, this);
    }

    @Subscribe
    public void onJoin(ServerPostConnectEvent event) {
        core.logger.warn(String.format("[Wolf-Core] Player joined: {0}", event.getPlayer().getUsername()));
        checkPlayer(event.getPlayer());
        onJoin(event.getPlayer());
    }

    public void onJoin(Player player) {
        YamlDocument data = core.getConfig(player.getUniqueId().toString(), core.dataDirectory.resolve("userdata"));
        if (data == null) {
            core.logger.warn(String.format("[Wolf-Core] Player data not found for {0}", player.getUsername()));
            player.disconnect(Component.text("§4§lError: §cPlayer data not found, please contact an administrator."));
        } else {
            PlayerData PlayerData = new PlayerData(player, data);
            players.put(player.getUniqueId(), PlayerData);
            data.set("timestamp.login", System.currentTimeMillis());
            Punishment punishment = PlayerData.isBanned();
            if (punishment != null) {
                player.disconnect(Component
                        .text("§4§lError: §cYou are banned from this server.\n§6Reason: §e" + punishment.reason));
            }
        }
    }

    @Subscribe
    public void onQuit(DisconnectEvent event) {
        Player player = event.getPlayer();
        PlayerData PlayerData = players.get(player.getUniqueId());
        if (PlayerData == null) {
            core.logger.warn(String.format("[Wolf-Core] Player left without data: {0}", player.getUsername()));
            return;
        }
        PlayerData.setLogout(System.currentTimeMillis());
        try {
            PlayerData.save();
        } catch (Exception e) {
            core.logger.warn(String.format("[Wolf-Core] Failed to save data for {0}", player.getUsername()));
        }
        if (players.remove(player.getUniqueId()) == null) {
            core.logger.warn(String.format("[Wolf-Core] Player left without data: {0}", player.getUsername()));
        }
    }

    public PlayerData getPlayerData(UUID uuid) {
        return players.get(uuid);
    }

    public PlayerData getPlayerData(Player player) {
        return players.get(player.getUniqueId());
    }

    public OfflinePlayer getOfflinePlayer(UUID uuid) {
        PlayerData playerData = getPlayerData(uuid);
        if (playerData != null) {
            return new OfflinePlayer(playerData.data);
        }
        YamlDocument data = core.getConfig(uuid.toString(), core.dataDirectory.resolve("userdata"));
        if (data == null) {
            return null;
        }
        return new OfflinePlayer(data);
    }

    public UUID checkPlayer(Player player) {
        if (!offlineData.contains(player.getUsername())) {
            offlineData.set(player.getUsername(), player.getUniqueId().toString());
            try {
                offlineData.save();
            } catch (IOException e) {
                core.logger.warn(e.getMessage());
            }
        }
        UUID uuid = UUID.fromString(offlineData.getString(player.getUsername()));
        return uuid;
    }

    public UUID checkPlayer(String player) {
        if (!offlineData.contains(player)) {
            return null;
        }
        UUID uuid = UUID.fromString(offlineData.getString(player));
        return uuid;
    }
}
