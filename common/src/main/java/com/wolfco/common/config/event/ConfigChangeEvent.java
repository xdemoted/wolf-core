package com.wolfco.common.config.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import dev.dejvokep.boostedyaml.YamlDocument;

public class ConfigChangeEvent extends Event {
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    private final YamlDocument config;

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public ConfigChangeEvent(YamlDocument config) {
        super(false);
        this.config = config;
    }

    public YamlDocument getConfig() {
        return config;
    }
}