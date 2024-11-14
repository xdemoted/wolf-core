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
import com.wolfco.main.commands.database;
import com.wolfco.main.commands.delhome;
import com.wolfco.main.commands.delwarp;
import com.wolfco.main.commands.gamemode;
import com.wolfco.main.commands.gamemodeAlias;
import com.wolfco.main.commands.home;
import com.wolfco.main.commands.max;
import com.wolfco.main.commands.sethome;
import com.wolfco.main.commands.setwarp;
import com.wolfco.main.commands.teleport;
import com.wolfco.main.commands.teleportaccept;
import com.wolfco.main.commands.teleportall;
import com.wolfco.main.commands.teleportask;
import com.wolfco.main.commands.teleportdeny;
import com.wolfco.main.commands.teleporthere;
import com.wolfco.main.commands.test;
import com.wolfco.main.commands.warp;
import com.wolfco.main.commands.warpinfo;
import com.wolfco.main.commands.warps;
import com.wolfco.main.events.ChatManager;
import com.wolfco.main.events.PlayerManager;
import com.wolfco.main.handlers.DatabaseHandler;

import dev.dejvokep.boostedyaml.YamlDocument;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;

public class Core extends CorePlugin implements Listener {

    public LuckPerms lp;
    public Utilities utils = new Utilities(this);
    public YamlDocument warps;
    public PlayerManager PlayerManager;
    public DatabaseHandler db;

    @Override
    public void onEnable() {
        try {
            lp = LuckPermsProvider.get();
        } catch (Exception e) {
            return;
        }
        getAdventure();
        config = getConfig("config.yml");
        warps = getConfig("warps.yml");
        try {
            db = new DatabaseHandler(this);
        } catch (SQLException e) {
            return;
        }

        commandLoader.registerAll(getCommands());
        PlayerManager = new PlayerManager(this);

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(this, this);
        pm.registerEvents((Listener) PlayerManager, this);
        pm.registerEvents((Listener) new ChatManager(this), this);

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "core:main");

        this.getLogger().info("[Wolf-Core] Plugin enabled");
    }

    @Override
    public void onDisable() {
        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(this, "core:main");
        this.getLogger().info("[Wolf-Core] Plugin disabled");
    }

    @Override
    public List<CoreCommandExecutor> getCommands() {
        List<CoreCommandExecutor> list = new ArrayList<>();
        list.add(new database(this));
        list.add(new delhome(this));
        list.add(new delwarp(this));
        list.add(new gamemode(this));
        list.add(new gamemodeAlias(this));
        list.add(new home(this));
        list.add(new max(this));
        list.add(new sethome(this));
        list.add(new setwarp(this));
        list.add(new teleport(this));
        list.add(new teleportaccept(this));
        list.add(new teleportall(this));
        list.add(new teleportask(this));
        list.add(new teleportdeny(this));
        list.add(new teleporthere(this));
        list.add(new test(this));
        list.add(new warp(this));
        list.add(new warpinfo(this));
        list.add(new warps(this));
        return list;
    }
}
