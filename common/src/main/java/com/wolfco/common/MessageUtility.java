package com.wolfco.common;

import java.io.IOException;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.wolfco.common.classes.CorePlugin;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

@Singleton
public class MessageUtility {
    YamlDocument messages;

    @Inject
    public MessageUtility() {

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

    public static MessageUtility getInstance() {
        return CorePlugin.get().getScope().get(MessageUtility.class);
    }

    public void sendMessage(CommandSender sender, String message) {
        CorePlugin.get().getAdventure().sender(sender).sendMessage(MiniMessage.miniMessage().deserialize(message));
    }

    public String getMessage(String key) {
        return messages.getString(key, key);
    }

    public Component getComponentMessage(String key) {
        return MiniMessage.miniMessage().deserialize(getMessage(key));
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
}
