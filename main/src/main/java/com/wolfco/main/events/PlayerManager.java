package com.wolfco.main.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.wolfco.main.Core;
import com.wolfco.main.classes.Request;
import com.wolfco.main.classes.mongoDB.GlobalPlayer;
import com.wolfco.main.classes.mongoDB.subtypes.NamedLocation;
import com.wolfco.main.classes.mongoDB.subtypes.Session;
import com.wolfco.main.classes.redis.OnlinePlayer;
import com.wolfco.main.handlers.MongoDatabase;
import com.wolfco.main.handlers.RedisManager;
import com.wolfco.main.handlers.TeamHandler;

public class PlayerManager implements Listener {
    Core core;
    TeamHandler teamHandler;
    RedisManager redisManager;
    MongoDatabase mongoDatabase;

    Map<UUID, List<Request>> tpaRequests = Map.of();
    Map<UUID, List<String>> homeCache = Map.of();

    public PlayerManager(Core core) {
        this.core = core;
        this.mongoDatabase = core.getDatabaseHandler();
        this.redisManager = core.getRedisManager();
        teamHandler = new TeamHandler(core);
        Collection<? extends Player> onlinePlayers = core.getServer().getOnlinePlayers();
        if (!onlinePlayers.isEmpty()) {
            for (Player player : onlinePlayers) {
                onJoin(new PlayerJoinEvent(player, null));
            }
        }
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player player = event.getPlayer();
        teamHandler.updatePrefix(event.getPlayer());

        redisManager.getPlayerAsync(player.getUniqueId().toString()).thenAccept(onlinePlayer -> {
            if (onlinePlayer == null) {
                mongoDatabase.getPlayer(player.getUniqueId().toString()).thenAccept(globalPlayer -> {
                    if (globalPlayer == null) {
                        globalPlayer = new GlobalPlayer(player);
                    }

                    globalPlayer.getSessions().add(new Session(player));

                    List<String> usernames = globalPlayer.getUsernames();

                    if (!usernames.contains(player.getName())) {
                        usernames.add(player.getName());
                    }

                    mongoDatabase.updatePlayer(globalPlayer);

                    OnlinePlayer activeData = new OnlinePlayer(globalPlayer);

                    activeData.setServer(redisManager.serverName);

                    redisManager.setPlayerAsync(globalPlayer);
                    redisManager.sendPlayerJoinAsync(globalPlayer);
                });
            } else {
                String oldServer = onlinePlayer.getServer();
                onlinePlayer.setServer(redisManager.serverName);
                onlinePlayer.setIsAFK(false);

                redisManager.setPlayerAsync(onlinePlayer);
                redisManager.sendPlayerSwitchAsync(onlinePlayer, oldServer);
            }
        }).exceptionally(ex -> {
            core.getLogger().log(Level.SEVERE, "[Wolf-Core] Error updating player data for {0}", player.getName());
            return null;
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskLater(core, () -> {
            CompletableFuture<OnlinePlayer> playerFuture = redisManager.getPlayerAsync(player.getUniqueId().toString());

            playerFuture.thenAccept(onlinePlayer -> {
                if (onlinePlayer != null) {
                    if (onlinePlayer.getServer().equals(redisManager.serverName)) {
                        redisManager.sendPlayerLeaveAsync(onlinePlayer);
                        mongoDatabase.updatePlayer(onlinePlayer);
                        redisManager.removePlayerAsync(player.getUniqueId().toString());
                    }
                }
            }).exceptionally(ex -> {
                core.getLogger().log(Level.SEVERE, "[Wolf-Core] Error updating player data for {0}", player.getName());
                return null;
            });
        }, 60L);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
    }

    public CompletableFuture<OnlinePlayer> getPlayerData(Player player) {
        return redisManager.getPlayerAsync(player.getUniqueId().toString());
    }

    public List<Request> getTpaRequests(Player player) {
        return tpaRequests.get(player.getUniqueId());
    }

    public List<String> getHomes(Player player) {
        var homes = homeCache.get(player.getUniqueId());

        if (homes == null) {
            redisManager.getPlayerAsync(player.getUniqueId().toString()).thenAccept(onlinePlayer -> {
                if (onlinePlayer != null) {
                    var homeData = onlinePlayer.getHomes();
                    List<String> validHomes = new ArrayList<>();

                    for (NamedLocation home : homeData) {
                        if (home.getServerName().equals(redisManager.serverName)) {
                            validHomes.add(home.getName());
                        }
                    }

                    homeCache.put(player.getUniqueId(), validHomes);
                }
            });

            return List.of();
        } else {
            return homes;
        }
    }
}
