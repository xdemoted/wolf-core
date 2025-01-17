package com.wolfco.velocity.modules;

import java.util.Collection;
import java.util.List;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.player.TabListEntry;
import com.wolfco.velocity.wolfcore;

import net.kyori.adventure.text.minimessage.MiniMessage;

public class tablist {
    public static void update(wolfcore core) {
        Collection<Player> players = core.server.getAllPlayers();
        List<TabListEntry> entries = players.stream()
                .map(player -> TabListEntry.builder()
                        .displayName(MiniMessage.miniMessage().deserialize(player.getUsername()))
                        .profile(player.getGameProfile())
                        .gameMode(0)
                        .tabList(player.getTabList())
                        .latency((int) (player.getPing() * 1000))
                        .build()).toList();
        for (Player player : core.server.getAllPlayers()) {
            player.getTabList().clearAll();
            player.getTabList().addEntries(entries);
        }
    }
}
