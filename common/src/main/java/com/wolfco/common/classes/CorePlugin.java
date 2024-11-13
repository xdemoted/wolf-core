package com.wolfco.common.classes;

import org.bukkit.plugin.java.JavaPlugin;

import com.wolfco.common.CommandLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.nio.file.Path;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;

public abstract class CorePlugin extends JavaPlugin {
    public BukkitAudiences adventure;
    public YamlDocument config;
    public YamlDocument messages;
    public CommandLoader commandLoader;
    static public String prefix = "♆ ";

    public CorePlugin() {
        messages = getMessageData();
        commandLoader = new CommandLoader(this);
        commandLoader.registerAll(getCommands());
    }

    public BukkitAudiences getAdventure() {
        if (this.adventure == null) {
            this.adventure = BukkitAudiences.create(this);
        }
        return this.adventure;
    }

    public abstract List<CoreCommandExecutor> getCommands();

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

    public void log(String log) {
        getLogger().info(log);
    }
}
