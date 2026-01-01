package com.wolfco.main.events;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.wolfco.common.listeners.CoreListener;
import com.wolfco.main.Core;
import com.wolfco.main.classes.PlayerData;
import com.wolfco.main.handlers.PlayerVisualsHandler;

import dev.dejvokep.boostedyaml.YamlDocument;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

@Named("playerManager")
@Singleton
public class PlayerManager implements CoreListener {
    static final List<TeleportCause> IGNORED_CAUSES = List.of( // Back listener
            TeleportCause.ENDER_PEARL,
            TeleportCause.CHORUS_FRUIT,
            TeleportCause.SPECTATE,
            TeleportCause.DISMOUNT,
            TeleportCause.END_PORTAL,
            TeleportCause.NETHER_PORTAL,
            TeleportCause.EXIT_BED,
            TeleportCause.END_GATEWAY);

    Map<UUID, PlayerData> players = new HashMap<>();
    Core core;

    @Inject
    public PlayerManager(Core core) {
        this.core = core;
        try {
            Collection<? extends Player> onlinePlayers = core.getServer().getOnlinePlayers();
            if (!onlinePlayers.isEmpty()) {
                for (Player player : onlinePlayers) {
                    onJoin(player);
                }
            }
        } catch (Exception e) {
            core.getLogger().log(Level.WARNING, "[Wolf-Core] Error loading online players during initialization", e);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.joinMessage(null);

        onJoin(event.getPlayer());
    }

    private void onJoin(Player player) {
        YamlDocument data = null;

        try {
            data = core.getConfigDocument(player.getUniqueId().toString(),
                    core.getDataFolder().toPath().resolve("userdata"));
        } catch (Exception e) {
            core.getLogger().log(Level.WARNING, "[Wolf-Core] Player data not found for {0}", player.getName());
            player.kickPlayer("§4§lError: §cPlayer data not found, please contact an administrator.");
        }
        if (data == null) {
            core.getLogger().log(Level.WARNING, "[Wolf-Core] Player data not found for {0}", player.getName());
            player.kickPlayer("§4§lError: §cPlayer data not found, please contact an administrator.");
            return;
        }

        data.set("timestamp.login", System.currentTimeMillis());

        players.put(player.getUniqueId(), new PlayerData(player, data));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.quitMessage(null);

        Player player = event.getPlayer();
        PlayerData playerData = players.get(player.getUniqueId());

        if (playerData == null) {
            core.getLogger().log(Level.WARNING, "[Wolf-Core] Player left without data: {0}", player.getName());
            return;
        }

        playerData.save();

        try {
            playerData.data.save();
        } catch (IOException ex) {
            core.getLogger().log(Level.SEVERE, "[Wolf-Core] Error saving player data for {0}", player.getName());
            return;
        }

        if (players.remove(player.getUniqueId()) == null) {
            core.getLogger().log(Level.WARNING, "[Wolf-Core] Player left without data: {0}", player.getName());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR) // Should always run last
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        PlayerData playerData = players.get(event.getPlayer().getUniqueId());

        if (!IGNORED_CAUSES.contains(event.getCause())) {
            if (playerData != null) {
                playerData.lastPosition = event.getFrom();
            }
        }
    }

    public static List<reducedPlayerInfo> getAllPlayerDataDocuments() {
        File directory = Core.get().getDataFolder().toPath().resolve("userdata").toFile();
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                List<reducedPlayerInfo> documents = new java.util.ArrayList<>();
                for (File file : files) {
                    try {
                        UUID uuid = UUID.fromString(file.getName().replace(".yml", ""));
                        YamlDocument document = Core.get().getConfigDocument(uuid.toString(),
                                directory.toPath());
                        String username = document.getString("username", null);
                        if (username != null)
                            documents.add(new reducedPlayerInfo(uuid, username));
                    } catch (Exception e) {
                        Core.get().getLogger().log(Level.WARNING,
                                "[Wolf-Core] Could not load player data file: {0}", file.getName());
                    }
                }
                return documents;
            }
        }
        return List.of();
    }

    public PlayerData getOfflinePlayer(UUID uuid) {
        YamlDocument data = null;
        try {
            data = core.getConfigDocument(uuid.toString(),
                    core.getDataFolder().toPath().resolve("userdata"));
        } catch (Exception e) {
            core.getLogger().log(Level.WARNING, "[Wolf-Core] Player data not found for {0}", uuid.toString());
        }
        if (data == null) {
            core.getLogger().log(Level.WARNING, "[Wolf-Core] Player data not found for {0}", uuid.toString());
        }

        return new PlayerData(null, data, true);
    }

    public PlayerData getPlayerData(Player player) {
        return players.get(player.getUniqueId());
    }

    public static class reducedPlayerInfo {
        public UUID uuid;
        public String name;

        public reducedPlayerInfo(UUID uuid, String name) {
            this.uuid = uuid;
            this.name = name;
        }
    }
}