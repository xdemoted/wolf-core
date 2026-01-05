package com.wolfco.main.whitelist;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import com.wolfco.common.listeners.CoreListener;
import com.wolfco.main.Core;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

@Named("whitelistListener")
@Singleton
public class WhitelistListener implements CoreListener {
    private final WhitelistManager whitelistManager;
    private final Core core;

    @Inject
    public WhitelistListener(Core core, WhitelistManager manager) {
        this.core = core;
        this.whitelistManager = manager;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        int status = whitelistManager.isWhitelisted(event.getPlayer());

        if (status == 0) {
            event.getPlayer().kick(
                core.getComponentMessage("whitelist.denied")
            );
        } else if (status == 2) {
            event.getPlayer().sendMessage(
                core.getMessage("whitelist.bypass")
            );
        }
    }
}
