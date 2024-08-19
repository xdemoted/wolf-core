package risingdeathx2.spigot.wolfcore;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import risingdeathx2.spigot.wolfcore.events.chat;
import risingdeathx2.spigot.wolfcore.events.playerManager;

public class core extends JavaPlugin implements Listener {
    public LuckPerms lp;
    public utils utils = new utils(this);
    private BukkitAudiences adventure;
    public YamlDocument config;
    public YamlDocument warps;
    public YamlDocument messages;
    public CommandLoader commandLoader;
    public playerManager playerManager;
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
        messages = getMessageData();
        config = getConfig("config.yml");
        warps = getConfig("warps.yml");
        this.getLogger().info("[Wolf-Core] Plugin enabled");
        commandLoader = new CommandLoader(this);
        commandLoader.loadCommands();
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

    public YamlDocument getConfig(String fileName) {
        return getConfig(fileName, getDataFolder().toPath());
    }

    public YamlDocument getConfig(String fileName, Path parent) {
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

    public String getMessage(String key) {
        return messages.getString(key, key);
    }

    public String getMessage(String key, List<String> input) {
        String message = messages.getString(key, key);
        for (int i = 0; i < input.size(); i++) {
            message = message.replace("%" + i + "%", input.get(i));
        }
        return message;
    }

    public YamlDocument getMessageData() {
        messages = getConfig("messages.yml");
        if (messages != null) {
            messages.setSettings(UpdaterSettings.builder().setVersioning(new BasicVersioning("version")).build());
            try {
                messages.update();
            } catch (Exception e) {
                getLogger().warning("Failed to update messages.yml");
            }
        }
        return messages;
    }
}