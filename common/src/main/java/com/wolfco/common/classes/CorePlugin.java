package com.wolfco.common.classes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.wolfco.common.commands.CommandLoader;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import io.avaje.inject.BeanScope;
import io.avaje.inject.InjectModule;
import jakarta.annotation.Nullable;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

@InjectModule(provides = { CorePlugin.class, JavaPlugin.class, Plugin.class })
public abstract class CorePlugin extends JavaPlugin {
    private final static List<String> SERVER_ICONS = List.of(
            "creative", "survival");
    private BeanScope scope;

    public String serverName;
    String icon = null;
    BukkitAudiences adventure;
    YamlDocument config;
    YamlDocument messages;

    protected CorePlugin() {
        messages = getMessageData();
    }

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
                // Register concrete plugin class (e.g., com.wolfco.main.Core) so @Inject Core
                // works
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

    public BukkitAudiences getAdventure() {
        if (this.adventure == null) {
            this.adventure = BukkitAudiences.create(this);
        }

        return this.adventure;
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

    public String getMessage(String key) {
        return messages.getString(key, key);
    }

    public void sendPreset(CommandSender sender, String key) {
        sendMessage(sender, messages.getString(key, key));
    }

    public void sendPreset(CommandSender sender, String key, List<String> input) {
        sendMessage(sender, getPreset(key, input));
    }

    public String getPreset(String key, List<String> input) {
        String message = messages.getString(key, key);

        for (int i = 0; i < input.size(); i++) {
            message = message.replaceAll("%" + i + "%", input.get(i));
        }

        return message.replace("♆", messages.getString("core.prefix", "♆"));
    }

    private YamlDocument getMessageData() {
        messages = getConfigDocument("messages.yml");
        if (messages != null) {
            messages.setSettings(UpdaterSettings.builder().setVersioning(new BasicVersioning("version")).build());
            try {
                messages.update();
            } catch (IOException e) {
                getLogger().warning("Failed to update messages.yml");
            }
        }
        return messages;
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

    public void sendMessage(CommandSender sender, String message) {
        getAdventure().sender(sender).sendMessage(MiniMessage.miniMessage().deserialize(message));
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
