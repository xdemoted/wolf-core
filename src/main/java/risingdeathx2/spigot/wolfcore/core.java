package risingdeathx2.spigot.wolfcore;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import dev.dejvokep.boostedyaml.YamlDocument;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import risingdeathx2.spigot.wolfcore.classes.PlayerData;
import risingdeathx2.spigot.wolfcore.events.chat;
import risingdeathx2.spigot.wolfcore.events.playerManager;

public class core extends JavaPlugin implements Listener {
    public LuckPerms lp;
    public utils utils = new utils(this);
    private BukkitAudiences adventure;
    public YamlDocument config;
    public YamlDocument warps;
    public HashMap<UUID,PlayerData> players = new HashMap<UUID,PlayerData>();
    static public String prefix = "♆ ";

    public @NonNull BukkitAudiences adventure() {
        if (this.adventure == null) {
            this.adventure = BukkitAudiences.create(this);
        }
        return this.adventure;
    }

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
        this.getLogger().info("[Wolf-Core] Plugin enabled");
        new command(this);
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(this, this);
        pm.registerEvents((Listener) new playerManager(this), this);
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
    
    public YamlDocument getConfig(String fileName) {
        return getConfig(fileName,getDataFolder().toPath());
    }
    public YamlDocument getConfig(String fileName,Path parent) {
        Path configFile = parent.resolve(fileName);
        YamlDocument config;
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName)) {
            config = YamlDocument.create(configFile.toFile(), is);
        } catch (IOException e) {
            try {
                config = YamlDocument.create(configFile.toFile());
            } catch (IOException e1) {
                config = null;
            }
        }
        return config;
    }
}