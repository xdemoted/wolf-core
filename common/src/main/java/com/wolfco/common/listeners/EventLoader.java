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

    public void registerAll() {
        PluginManager pm = core.getServer().getPluginManager();
        List<CoreListener> listeners = core.getScope().list(CoreListener.class);

        core.log("Registering " + listeners.size() + " event listeners");

        if (listeners.isEmpty()) {
            core.getLogger().warning("No event listeners discovered; check avaje-inject module wiring.");
            return;
        }

        for (CoreListener listener : listeners) {
            pm.registerEvents(listener, core);
            core.log("Registered listener: " + listener.getClass().getName());
        }
    }
}
