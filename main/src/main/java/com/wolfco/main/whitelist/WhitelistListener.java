package com.wolfco.main.whitelist;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.wolfco.common.listeners.CoreListener;
import com.wolfco.main.Core;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class WhitelistListener implements CoreListener {
    private final Core core;

    @Inject
    public WhitelistListener(Core core) {
        this.core = core;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        core.log("whitelist check");
    }

    @EventHandler
    public void onPortal(PlayerPortalEvent event) {
        core.log("from: " + event.getFrom().toString() + " to: " + event.getTo().toString());
        core.log("cause: " + event.getCause().toString());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTeleport(PlayerTeleportEvent event) {
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.UNKNOWN) {
            if (event.getFrom().getWorld().getEnvironment() == World.Environment.THE_END
                    && event.getFrom().getBlock().getType() == Material.END_PORTAL) {
                event.setCancelled(true);
                event.getPlayer().teleport(event.getTo(),
                        PlayerTeleportEvent.TeleportCause.END_PORTAL);
            }
        }
    }

    @EventHandler
    public void onTeleport2(PlayerTeleportEvent event) {
        core.log("from: " + event.getFrom().toString() + " to: " + event.getTo().toString());
        core.log("cause: " + event.getCause().toString());
    }
}
