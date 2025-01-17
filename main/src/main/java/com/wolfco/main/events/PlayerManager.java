package com.wolfco.main.events;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.wolfco.main.Core;
import com.wolfco.main.classes.PlayerData;
import com.wolfco.main.handlers.TeamHandler;

import dev.dejvokep.boostedyaml.YamlDocument;

public class PlayerManager implements Listener {

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
        onJoin(event.getPlayer());
        teamHandler.updatePrefix(event.getPlayer());
    }

    private void onJoin(Player player) {
        YamlDocument data = core.getConfigDocument(player.getUniqueId().toString(), core.getDataFolder().toPath().resolve("userdata"));
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
        Player player = event.getPlayer();
        PlayerData playerData = players.get(player.getUniqueId());

        if (playerData == null) {
            core.getLogger().log(Level.WARNING, "[Wolf-Core] Player left without data: {0}", player.getName());
            return;
        }

        YamlDocument data = playerData.data;
        data.set("timestamp.logout", System.currentTimeMillis());

        try {
            data.save();
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
            core.getChatManager().sendGlobalBroadcast(player, "§8[§aNetwork§8]§a " + player.getName() + " §eis no longer AFK.");
        }
    }

    public PlayerData getPlayerData(Player player) {
        return players.get(player.getUniqueId());
    }
}
