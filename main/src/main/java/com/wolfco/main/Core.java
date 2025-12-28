package com.wolfco.main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.classes.CorePlugin;
import com.wolfco.main.commands.Back;
import com.wolfco.main.commands.DelHome;
import com.wolfco.main.commands.DelWarp;
import com.wolfco.main.commands.Enchant;
import com.wolfco.main.commands.Fly;
import com.wolfco.main.commands.FlySpeed;
import com.wolfco.main.commands.Gamemode;
import com.wolfco.main.commands.GamemodeAlias;
import com.wolfco.main.commands.Home;
import com.wolfco.main.commands.InventorySee;
import com.wolfco.main.commands.Max;
import com.wolfco.main.commands.MiniMessage;
import com.wolfco.main.commands.OfflineTeleport;
import com.wolfco.main.commands.Reach;
import com.wolfco.main.commands.SetHome;
import com.wolfco.main.commands.SetWarp;
import com.wolfco.main.commands.Speed;
import com.wolfco.main.commands.Teleport;
import com.wolfco.main.commands.TeleportAccept;
import com.wolfco.main.commands.TeleportAll;
import com.wolfco.main.commands.TeleportAsk;
import com.wolfco.main.commands.TeleportDeny;
import com.wolfco.main.commands.TeleportHere;
import com.wolfco.main.commands.Test;
import com.wolfco.main.commands.Top;
import com.wolfco.main.commands.WalkSpeed;
import com.wolfco.main.commands.Warp;
import com.wolfco.main.commands.WarpInfo;
import com.wolfco.main.commands.Warps;
import com.wolfco.main.commands.WorldCMD;
import com.wolfco.main.events.ChatManager;
import com.wolfco.main.events.PlayerManager;
import com.wolfco.main.events.RedisManager;
import com.wolfco.main.handlers.MongoDatabase;
import com.wolfco.main.handlers.WebhookManager;

import dev.dejvokep.boostedyaml.YamlDocument;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;

public class Core extends CorePlugin implements Listener {
    LuckPerms lp;
    YamlDocument warps;
    PlayerManager playerManager;
    PluginManager pluginManager;
    RedisManager redisManager;
    MongoDatabase db;

    List<Player> afkPlayers = new ArrayList<>();

    @Override
    public void onEnable() {
        try {
            lp = LuckPermsProvider.get();
        } catch (Exception e) {
            return;
        }

        getAdventure();

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
}
