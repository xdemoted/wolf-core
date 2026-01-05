package com.wolfco.main.whitelist;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import com.wolfco.common.MessageUtility;
import com.wolfco.common.listeners.CoreListener;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

@Named("whitelistListener")
@Singleton
public class WhitelistListener implements CoreListener {
    private final WhitelistManager whitelistManager;
    private final MessageUtility messageUtility;

    @Inject
    public WhitelistListener(WhitelistManager manager, MessageUtility messageUtility) {
        this.whitelistManager = manager;
        this.messageUtility = messageUtility;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        int status = whitelistManager.isWhitelisted(event.getPlayer());

        if (status == 0) {
            event.getPlayer().kick(
                messageUtility.getComponentMessage("whitelist.denied")
            );
        } else if (status == 2) {
            event.getPlayer().sendMessage(
                messageUtility.getMessage("whitelist.bypass")
            );
        }
    }
}
