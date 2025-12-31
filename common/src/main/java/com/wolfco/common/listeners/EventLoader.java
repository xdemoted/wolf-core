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
    private List<CoreListener> listeners;

    public void registerAll() {
        this.listeners = core.getScope().list(CoreListener.class);
        PluginManager pm = core.getServer().getPluginManager();

        core.log("Registering " + listeners.size() + " event listeners");

        if (listeners.isEmpty()) {
            core.getLogger().warning("No event listeners discovered;");
            return;
        }

        for (CoreListener listener : listeners) {
            pm.registerEvents(listener, core);
            core.log("Registered listener: " + listener.getClass().getName());
        }
    }
}
