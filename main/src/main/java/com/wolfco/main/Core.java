package com.wolfco.main;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import com.wolfco.common.Utilities;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.CorePlugin;
import com.wolfco.main.commands.Database;
import com.wolfco.main.commands.DelHome;
import com.wolfco.main.commands.DelWarp;
import com.wolfco.main.commands.Gamemode;
import com.wolfco.main.commands.GamemodeAlias;
import com.wolfco.main.commands.Home;
import com.wolfco.main.commands.Max;
import com.wolfco.main.commands.SetHome;
import com.wolfco.main.commands.SetWarp;
import com.wolfco.main.commands.Teleport;
import com.wolfco.main.commands.TeleportAccept;
import com.wolfco.main.commands.TeleportAll;
import com.wolfco.main.commands.TeleportAsk;
import com.wolfco.main.commands.TeleportDeny;
import com.wolfco.main.commands.TeleportHere;
import com.wolfco.main.commands.Test;
import com.wolfco.main.commands.Warp;
import com.wolfco.main.commands.WarpInfo;
import com.wolfco.main.commands.Warps;
import com.wolfco.main.events.ChatManager;
import com.wolfco.main.events.PlayerManager;
import com.wolfco.main.handlers.DatabaseHandler;

import dev.dejvokep.boostedyaml.YamlDocument;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;

public class Core extends CorePlugin implements Listener {

     LuckPerms lp;
     Utilities utils = new Utilities(this);
     YamlDocument warps;
     PlayerManager playerManager;
     DatabaseHandler db;

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

        try {
            db = new DatabaseHandler(this);
        } catch (SQLException e) {
            return;
        }

        getCommandLoader().registerAll(getCommands());
        playerManager = new PlayerManager(this);

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(this, this);
        pm.registerEvents(playerManager, this);
        pm.registerEvents(new ChatManager(this), this);

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "core:main");

        this.getLogger().info("[Wolf-Core] Plugin enabled");
    }

    public DatabaseHandler getDatabaseHandler() {
        return db;
    }

    public LuckPerms getLuckPerms() {
        return lp;
    }

    public Utilities getUtilities() {
        return utils;
    }

    public YamlDocument getWarps() {
        return warps;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    @Override
    public void onDisable() {
        BukkitAudiences adventure = getAdventure();

        if (adventure != null) {
            adventure.close();
        }

        Bukkit.getMessenger().unregisterOutgoingPluginChannel(this, "core:main");
        this.getLogger().info("[Wolf-Core] Plugin disabled");
    }

    @Override
    public List<CoreCommandExecutor> getCommands() {
        List<CoreCommandExecutor> list = new ArrayList<>();
        list.add(new Database(this));
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
        return list;
    }
}
