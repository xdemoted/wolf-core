package com.wolfco.main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.wolfco.common.classes.CorePlugin;
import com.wolfco.main.events.ChatManager;
import com.wolfco.main.events.PlayerManager;
import com.wolfco.main.events.RedisManager;
import com.wolfco.main.handlers.MongoDatabase;
import com.wolfco.main.handlers.WebhookManager;

import dev.dejvokep.boostedyaml.YamlDocument;
import jakarta.inject.Singleton;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.luckperms.api.LuckPerms;

@Singleton
public class Core extends CorePlugin implements Listener {
    private static Core instance;
    LuckPerms lp;
    YamlDocument warps;
    PlayerManager playerManager;
    PluginManager pluginManager;
    RedisManager redisManager;
    MongoDatabase db;

    List<Player> afkPlayers = new ArrayList<>();

    @Override
    public void onStart() {
        instance = this;

        getAdventure();

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager()
                .getRegistration(LuckPerms.class);

        if (provider != null) {
            lp = provider.getProvider();
        } else {
            getLogger().severe("LuckPerms not found! Disabling plugin.");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        setMainConfig(getConfigDocument("config.yml"));
        serverName = getMainConfig().getString("server-name", "unknown");
        warps = getConfigDocument("warps.yml");

        this.pluginManager = Bukkit.getPluginManager();

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "core:main");
        this.redisManager = RedisManager.getInstance(serverName);

        this.getLogger().info("[Wolf-Core] Plugin horny");

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
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    public String getServerName() {
        return serverName;
    }

    public RedisManager getRedisManager() {
        return redisManager;
    }

    public MongoDatabase getDatabaseHandler() {
        return db;
    }

    public LuckPerms getLuckPerms() {
        return lp;
    }

    public YamlDocument getWarps() {
        return warps;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public ChatManager getChatManager() {
        return new ChatManager(this);
    }

    @Override
    public void onDisable() {
        BukkitAudiences adventure = getAdventure();

        if (adventure != null) {
            adventure.close();
        }

        instance = null;

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
        return instance;
    }
}
