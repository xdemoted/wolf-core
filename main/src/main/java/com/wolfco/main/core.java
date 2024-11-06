package com.wolfco.main;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import dev.dejvokep.boostedyaml.YamlDocument;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import com.wolfco.main.events.chat;
import com.wolfco.main.events.playerManager;
import com.wolfco.main.utilities.databaseHandler;
import com.wolfco.main.commands.*;
import com.wolfco.common.utils;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.CorePlugin;
import com.wolfco.common.commandLoader;

public class core extends CorePlugin implements Listener {
    public LuckPerms lp;
    public utils utils = new utils(this);
    private BukkitAudiences adventure;
    public YamlDocument config;
    public YamlDocument warps;
    public playerManager playerManager;
    public databaseHandler db;

    @Override
    public void onEnable() {
        try {
            lp = LuckPermsProvider.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        adventure();
        config = getConfig("config.yml");
        warps = getConfig("warps.yml");
        try {
            db = new databaseHandler(this);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        commandLoader commandLoader = new commandLoader(this);
        commandLoader.registerAll(getCommands());
        this.getLogger().info("[Wolf-Core] Plugin enabled");
        playerManager = new playerManager(this);
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(this, this);
        pm.registerEvents((Listener) playerManager, this);
        pm.registerEvents((Listener) new chat(this), this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "core:main");
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

    public static void log(String log) {
        Plugin plugin = core.getPlugin(core.class);
        plugin.getLogger().info(log);
    }
}