package com.wolfco.main.events;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.wolfco.common.Utilities;
import com.wolfco.main.Core;
import com.wolfco.main.classes.PlayerData;
import com.wolfco.main.handlers.TeamHandler;

import dev.dejvokep.boostedyaml.YamlDocument;

public class PlayerManager implements Listener {
    static final List<TeleportCause> IGNORED_CAUSES = List.of(
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
    TeamHandler teamHandler;

    public PlayerManager(Core core) {
        this.core = core;
        teamHandler = new TeamHandler(core);
        Collection<? extends Player> onlinePlayers = core.getServer().getOnlinePlayers();
        if (!onlinePlayers.isEmpty()) {
            for (Player player : onlinePlayers) {
                onJoin(player);
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        onJoin(event.getPlayer());
        teamHandler.updatePrefix(event.getPlayer());
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
        event.setQuitMessage(null);

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

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (core.getAfkPlayers().contains(player)) {
            core.getAfkPlayers().remove(player);
            core.getChatManager().sendGlobalBroadcast(player,
                    "§8[§aNetwork§8]§a " + player.getName() + " §eis no longer AFK.");
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        TeleportCause cause = event.getCause();
        PlayerData playerData = players.get(player.getUniqueId());

        Location from = event.getFrom();

        if (!IGNORED_CAUSES.contains(cause)) {
            if (playerData != null) {
                playerData.lastPosition[0] = from.getX();
                playerData.lastPosition[1] = from.getY();
                playerData.lastPosition[2] = from.getZ();
                playerData.lastPosition[3] = from.getYaw();
                playerData.lastPosition[4] = from.getPitch();
                playerData.lastWorld = from.getWorld().getUID();
            }
        } else if (cause == TeleportCause.ENDER_PEARL) {
            Location to = event.getTo();

            if (to == null)
                return;

            Double blockX = to.getBlockX() + 0.5;
            Double blockZ = to.getBlockZ() + 0.5;

            if (to.getX() - blockX < 0) {
                to.setX(Math.max(to.getX(), blockX - 0.2));
            } else {
                to.setX(Math.min(to.getX(), blockX + 0.2));
            }

            if (to.getZ() - blockZ < 0) {
                to.setZ(Math.max(to.getZ(), blockZ - 0.2));
            } else {
                to.setZ(Math.min(to.getZ(), blockZ + 0.2));
            }

            Block block = from.getBlock();
            Block upperBlock = block.getRelative(0, 1, 0);
            if (block.getType().isSolid() && upperBlock.getType().isSolid()) {
                player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL));
                Core.get().sendPreset(player, "enderpearl.blocked", List.of("from"));
                event.setCancelled(true);
                return;
            }

            block = to.getBlock();
            upperBlock = block.getRelative(0, 1, 0);

            if (block.getType().isSolid()) {
                Vector vector = Utilities.getUnitLocation(from, to).multiply(0.3);
                int iterations = 0;
                while ((!to.getBlock().isPassable()) && iterations < 11) {
                    to.subtract(vector);
                    iterations++;
                }
                if (iterations >= 11) {
                    player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL));
                    Core.get().sendPreset(player, "enderpearl.blocked", List.of("to"));
                    event.setCancelled(true);
                    return;
                }
            }

            if (upperBlock.getType().isSolid()) {
                to.setY(Math.min(to.getY(), upperBlock.getY() - 1));
            }
            event.setTo(to);
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