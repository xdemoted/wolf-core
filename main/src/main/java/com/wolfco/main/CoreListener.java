package com.wolfco.main;

import org.bukkit.event.Listener;

public class CoreListener implements Listener {
    final public Core core;

    public CoreListener() {
        core = (Core) Core.get();
        registerEvents();
    }

    void registerEvents() {
        core.getPluginManager().registerEvents(this, core);
    }
}
