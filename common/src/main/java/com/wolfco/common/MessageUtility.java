package com.wolfco.common;

import java.io.IOException;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.wolfco.common.classes.CorePlugin;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import jakarta.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

@Singleton
public class MessageUtility {
    static YamlDocument messages = null;

    public static YamlDocument getMessagesDocument() {
        if (messages == null) {
            messages = CorePlugin.get().getConfigDocument("messages.yml");
            if (messages != null) {
                messages.setSettings(UpdaterSettings.builder().setVersioning(new BasicVersioning("version")).build());
                try {
                    messages.update();
                } catch (IOException e) {
                    CorePlugin.get().getLogger().warning("Failed to update messages.yml");
                }
            }
        }

        return messages;
    }

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(MiniMessage.miniMessage().deserialize(message));
    }

    public static String getMessage(String key) {
        return getMessagesDocument().getString(key, key);
    }

    public static Component getComponentMessage(String key) {
        return MiniMessage.miniMessage().deserialize(getMessage(key));
    }

    public static void sendPreset(CommandSender sender, String key) {
        sendMessage(sender, getMessagesDocument().getString(key, key));
    }

    public static void sendPreset(CommandSender sender, String key, List<String> input) {
        sendMessage(sender, getPreset(key, input));
    }

    public static String getPreset(String key, List<String> input) {
        String message = getMessagesDocument().getString(key, key);

        for (int i = 0; i < input.size(); i++) {
            message = message.replaceAll("%" + i + "%", input.get(i));
        }

        return message.replace("♆", messages.getString("core.prefix", "♆"));
    }
}
