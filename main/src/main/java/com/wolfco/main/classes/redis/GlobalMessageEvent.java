package com.wolfco.main.classes.redis;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GlobalMessageEvent extends Event {
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    private final ChatMessage chatMessage;

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public GlobalMessageEvent(ChatMessage chatMessage) {
        super(true);
        this.chatMessage = chatMessage;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }
}
