package com.wolfco.main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.CorePlugin;
import com.wolfco.main.commands.DelHome;
import com.wolfco.main.commands.DelWarp;
import com.wolfco.main.commands.Fly;
import com.wolfco.main.commands.FlySpeed;
import com.wolfco.main.commands.Gamemode;
import com.wolfco.main.commands.GamemodeAlias;
import com.wolfco.main.commands.Home;
import com.wolfco.main.commands.Max;
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
import com.wolfco.main.handlers.MongoDatabase;
import com.wolfco.main.handlers.RedisManager;
import com.wolfco.main.handlers.WebhookManager;

import dev.dejvokep.boostedyaml.YamlDocument;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;

public class Core extends CorePlugin implements Listener {

    LuckPerms lp;
    YamlDocument warps;
    PlayerManager playerManager;
    MongoDatabase mongoDatabase;
    RedisManager redisManager;

    @Override
    public void onEnable() {
        try {
            lp = LuckPermsProvider.get();
        } catch (Exception e) {
            return;
        }

        getAdventure();

        setMainConfig(getConfigDocument("config.yml"));

        warps = getConfigDocument("warps.yml");

        registerCommands();

        /*
        try {
            mongoDatabase = new MongoDatabase(this);
            redisManager = new RedisManager(getMainConfig().getString("server-name", "Unknown Server"));
        } catch (IOException e) {
            Bukkit.getPluginManager().disablePlugin(this);
        }
        */

        playerManager = new PlayerManager(this);

        registerEvents();

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "core:main");

        Bukkit.getScheduler().runTaskLater(this, () -> {
            sendStartUpMessage();
            this.getLogger().info("[Wolf-Core] Plugin enabled");
        }, 20L * 10L);
    }

    @Override
    public void onDisable() {
        redisManager.sendSystemMessage("offline");
        redisManager.close();

        mongoDatabase.close();

        getAdventure().close();

        Bukkit.getMessenger().unregisterOutgoingPluginChannel(this, "core:main");

        this.getLogger().info("[Wolf-Core] Plugin disabled");

        sendShutDownMessage();
    }

    @Override
    public List<CoreCommandExecutor> getCommands() {
        List<CoreCommandExecutor> list = new ArrayList<>();
        list.add(new DelHome(this));
        list.add(new DelWarp(this));
        list.add(new Gamemode(this));
        list.add(new GamemodeAlias(this));
        list.add(new Home(this));
        list.add(new Max(this));
        list.add(new SetHome(this));
        list.add(new SetWarp(this));
        list.add(new Teleport(this));
        list.add(new TeleportAccept(this));
        list.add(new TeleportAll(this));
        list.add(new TeleportAsk(this));
        list.add(new TeleportDeny(this));
        list.add(new TeleportHere(this));
        list.add(new Test(this));
        list.add(new Warp(this));
        list.add(new WarpInfo(this));
        list.add(new Warps(this));
        list.add(new Top(this));
        list.add(new FlySpeed(this));
        list.add(new WalkSpeed(this));
        list.add(new Speed(this));
        list.add(new Fly(this));
        list.add(new WorldCMD(this));
        list.add(new Reach(this));
        return list;
    }

    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(this, this);
        pm.registerEvents(playerManager, this);
        pm.registerEvents(new ChatManager(this), this);
    }

    private void registerCommands() {
        getCommandLoader().registerAll(getCommands());
    }

    private void sendStartUpMessage() {
        WebhookManager webhook = new WebhookManager(this);

        String out = "# " + getMainConfig().getString("server-name", "Unknown Server") + " has started up.\n\n";

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
    }

    private void sendShutDownMessage() {
        WebhookManager webhook = new WebhookManager(this);
        webhook.sendLog("# " + getMainConfig().getString("server-name", "Unknown Server") + " has shutdown.");
    }

    public String getServerName() {
        return getMainConfig().getString("server-name", "Unknown Server");
    }

    public MongoDatabase getDatabaseHandler() {
        return mongoDatabase;
    }

    public RedisManager getRedisManager() {
        return redisManager;
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

    public void disable() {
        this.log("Forced disable called internally.");
        Bukkit.getPluginManager().disablePlugin(this);
    }

    public void disable(String reason) {
        this.log("Forced disable called internally. Reason: " + reason);
        Bukkit.getPluginManager().disablePlugin(this);
    }
}
