package com.wolfco.common.listeners;

import java.util.List;

import org.bukkit.plugin.PluginManager;

import com.wolfco.common.classes.CorePlugin;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class EventLoader {
    @Inject
    public CorePlugin core;

    @Inject
    public List<CoreListener> listeners;

    public void registerAll() {
        PluginManager pm = core.getServer().getPluginManager();

        for (CoreListener listener : listeners) {
            pm.registerEvents(listener, core);
        }
    }
}
