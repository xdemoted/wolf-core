package com.wolfco.main.profiles;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.wolfco.common.listeners.CoreListener;
import com.wolfco.main.Core;
import com.wolfco.main.profiles.classes.Profile;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class ProfileListener implements CoreListener {
    static final List<TeleportCause> IGNORED_CAUSES = List.of( // Back listener
            TeleportCause.ENDER_PEARL,
            TeleportCause.CHORUS_FRUIT,
            TeleportCause.SPECTATE,
            TeleportCause.DISMOUNT,
            TeleportCause.END_PORTAL,
            TeleportCause.NETHER_PORTAL,
            TeleportCause.EXIT_BED,
            TeleportCause.END_GATEWAY);

    private final ProfileManager profileManager;
    private final Core core;

    @Inject
    public ProfileListener(ProfileManager profileManager, Core core) {
        this.profileManager = profileManager;
        this.core = core;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.joinMessage(null);

        Profile profile = profileManager.loadProfile(event.getPlayer());
        if (!profileManager.cacheProfile(profile))
            core.getLogger().log(Level.WARNING, "[Wolf-Core] Failed to cache profile for {0}",
                    event.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.quitMessage(null);

        Profile profile = profileManager.getCachedProfile(event.getPlayer());

        if (profile == null) {
            core.log("Missing player data: ", event.getPlayer().getName());
            return;
        }

        try {
            profile.save();
        } catch (IOException e) {
            core.log("Failed to save player: ", event.getPlayer().getName());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Profile profile = profileManager.getCachedProfile(event.getPlayer());

        if (!IGNORED_CAUSES.contains(event.getCause())) {
            if (profile != null) {
                profile.lastPosition = event.getFrom();
            }
        }
    }
}