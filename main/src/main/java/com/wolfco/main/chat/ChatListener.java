package com.wolfco.main.chat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import com.wolfco.common.listeners.CoreListener;

import io.papermc.paper.event.player.AsyncChatEvent;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

@Named("chatListener")
@Singleton
public class ChatListener implements CoreListener {
    private final ChatManager chatManager;

    public ChatListener(ChatManager chatManager) {
        this.chatManager = chatManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(AsyncChatEvent event) {
        chatManager.handleChat(event.getPlayer(), event.message());
        event.setCancelled(true);
    }
}
