package com.wolfco.main.whitelist;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.wolfco.main.CoreListener;

public class WhitelistListener extends CoreListener {
    public WhitelistListener() {
        super();
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        core.log("whitelist check");
    }
}
