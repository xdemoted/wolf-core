package com.wolfco.main;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.wolfco.common.classes.CorePlugin;
import com.wolfco.common.listeners.EventLoader;

import jakarta.inject.Singleton;
import net.luckperms.api.LuckPerms;

@Singleton
public class Core extends CorePlugin implements Listener {
    LuckPerms lp;

    @Override
    public void onStart() {
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager()
                .getRegistration(LuckPerms.class);

        if (provider != null) {
            lp = provider.getProvider();
        } else {
            getLogger().severe("LuckPerms not found! Disabling plugin.");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        EventLoader eventLoader = getScope().get(EventLoader.class);
        eventLoader.registerAll();
    }

    public String getServerName() {
        return serverName;
    }

    public LuckPerms getLuckPerms() {
        return lp;
    }

    @Override
    public void onDisable() {
    }

    public static Core get() {
        return (Core) CorePlugin.get();
    }
}
