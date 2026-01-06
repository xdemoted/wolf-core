package com.wolfco.common.classes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.wolfco.common.commands.CommandLoader;
import com.wolfco.common.listeners.EventLoader;

import dev.dejvokep.boostedyaml.YamlDocument;
import io.avaje.inject.BeanScope;
import io.avaje.inject.InjectModule;
import io.avaje.inject.PostConstruct;
import jakarta.annotation.Nullable;
import net.kyori.adventure.text.Component;

@InjectModule(provides = { CorePlugin.class, JavaPlugin.class, Plugin.class })
public abstract class CorePlugin extends JavaPlugin {
    private final static List<String> SERVER_ICONS = List.of(
            "creative", "survival");
    private BeanScope scope;

    public String serverName;
    String icon = null;
    YamlDocument config;

    @Override
    public void onEnable() {
        setMainConfig(getConfigDocument("config.yml"));
        serverName = getMainConfig().getString("server-name", "unknown");

        scope = BeanScope.builder()
                .bean(this.getName(), Plugin.class, this)
                .bean(this.getName(), JavaPlugin.class, this)
                .bean(JavaPlugin.class, this)
                .bean(Plugin.class, this)
                .bean(CorePlugin.class, this)
                .bean((Class<CorePlugin>) getClass(), this)
                .classLoader(getClass().getClassLoader())
                .build();

        CommandLoader commandLoader = scope.get(CommandLoader.class);
        commandLoader.registerAll();

        onStart();
    }

    public abstract void onStart();

    public BeanScope getScope() {
        return scope;
    }

    public YamlDocument setMainConfig(YamlDocument config) {
        this.config = config;
        return config;
    }

    public YamlDocument getMainConfig() {
        return config;
    }

    @Nullable
    public YamlDocument getConfigDocument(String fileName) {
        return getConfigDocument(fileName, getDataFolder().toPath());
    }

    @Nullable
    public YamlDocument getConfigDocument(String fileName, Path parent) {
        Path configFile = parent.resolve(fileName);
        YamlDocument configReturn;
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName)) {
            configReturn = YamlDocument.create(configFile.toFile(), is);
        } catch (IOException e) {
            try {
                configReturn = YamlDocument.create(configFile.toFile());
            } catch (IOException e1) {
                configReturn = null;
            }
        }
        return configReturn;
    }

    public String getIcon() {
        if (icon == null) {
            if (SERVER_ICONS.contains(serverName.toLowerCase())) {
                icon = serverName.toLowerCase();
            } else {
                icon = "unknown";
            }
        }
        return icon;
    }

    public void log(Component log) {
        getComponentLogger().info(log);
    }

    public void log(String log, Object... args) {
        getLogger().info(String.format(log, args));
    }

    public static CorePlugin get() {
        return CorePlugin.getPlugin(CorePlugin.class);
    }
}
