package com.wolfco.main.whitelist;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import com.wolfco.common.listeners.CoreListener;
import com.wolfco.main.Core;

import jakarta.inject.Inject;

public class WhitelistListener implements CoreListener {
    private final Core core;

    @Inject
    public WhitelistListener(Core core) {
        this.core = core;
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        core.log("whitelist check");
    }
}
