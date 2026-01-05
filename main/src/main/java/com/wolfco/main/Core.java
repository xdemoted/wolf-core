package com.wolfco.main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.wolfco.common.classes.CorePlugin;
import com.wolfco.common.listeners.EventLoader;
import com.wolfco.main.utility.WebhookManager;

import jakarta.inject.Singleton;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.luckperms.api.LuckPerms;

@Singleton
public class Core extends CorePlugin implements Listener {
    LuckPerms lp;

    List<Player> afkPlayers = new ArrayList<>();

    @Override
    public void onStart() {
        getAdventure();

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager()
                .getRegistration(LuckPerms.class);

        if (provider != null) {
            lp = provider.getProvider();
        } else {
            getLogger().severe("LuckPerms not found! Disabling plugin.");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "core:main");

        Bukkit.getScheduler().runTaskLater(this, () -> {
            WebhookManager webhook = new WebhookManager(this);

            String out = "# " + serverName + " has started up.\n\n";

            out += "## Plugins loaded:\n";

            for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                String message = "- **" + plugin.getName() + "** v" + plugin.getDescription().getVersion();

                if (plugin.isEnabled() == false) {
                    message += " **(Disabled)**";
                }
                message += "\n";

                out += message;
            }

            out += "\n## [ End Log ]";

            webhook.sendLog(out);
        }, 20L * 10L);

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
        BukkitAudiences adventure = getAdventure();

        if (adventure != null) {
            adventure.close();
        }

        Bukkit.getMessenger().unregisterOutgoingPluginChannel(this, "core:main");
        this.getLogger().info("[Wolf-Core] Plugin disabled");

        WebhookManager webhook = new WebhookManager(this);

        webhook.sendLog("# " + serverName + " has shutdown.");
    }

    public List<Player> getAfkPlayers() {
        return afkPlayers;
    }

    public void addAfkPlayer(Player player) {
        afkPlayers.add(player);
    }

    public void removeAfkPlayer(Player player) {
        afkPlayers.remove(player);
    }

    public static Core get() {
        return (Core) CorePlugin.get();
    }
}
