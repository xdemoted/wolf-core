package com.wolfco.main.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.bukkit.entity.Player;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.wolfco.main.Core;

public class WebhookManager {
    Core core;

    public WebhookManager(Core core) {
        this.core = core;
    }

    public String filterMessage(String message) {
        return message.replaceAll("@", "`@`").replaceAll("#", "`#`");
    }

    public void sendMessage(Player player, String message) {
        sendMessage(player, message, true);
    }

    public void sendMessage(Player player, String message, boolean useFilter) {
        String stringURL = core.getMainConfig().getString("webhook.general", "replace");

        if (stringURL.equals("replace")) {
            core.getLogger().warning("Webhook URL not set in config.yml");
            return;
        }

        if (useFilter) {
            message = filterMessage(message);
        }

        String userName = player.getName();
        String avatarURL = "https://crafthead.net/helm/" + player.getUniqueId();

        sendWebhookMessage(avatarURL, userName, message, stringURL);
    }

    public void sendLog(String message) {
        String stringURL = core.getMainConfig().getString("webhook.logging", "replace");
        sendWebhookMessage("https://files.catbox.moe/7jm6w5.png", "Logger", message, stringURL);
    }

    public void sendWebhookMessage(String avatar_url, String username, String content, String stringURL) {

        if (stringURL.equals("replace")) {
            core.getLogger().warning("Webhook URL not set in config.yml");
            return;
        }

        try {
            JsonObject json = new JsonObject();

            json.addProperty("content", content);
            json.addProperty("username", username);
            json.addProperty("avatar_url", avatar_url);

            URL url;

            try {
                url = new URI(stringURL).toURL();
            } catch (MalformedURLException | URISyntaxException ex) {
                return;
            }

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.addRequestProperty("Content-Type", "application/json");
            connection.addRequestProperty("User-Agent", "Java-DiscordWebhook-BY-Gelox_");
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            try (OutputStream stream = connection.getOutputStream()) {
                stream.write(json.toString().getBytes());
                stream.flush();
            }

            connection.getInputStream().close(); // I'm not sure why but it doesn't work without getting the InputStream
            connection.disconnect();
        } catch (IOException ex) {
        }
    }

    public JsonObject createEmbed(String title, String description, Map<String,String> fields, String color, Boolean inline) {
        JsonObject embed = new JsonObject();
        embed.addProperty("title", title);
        embed.addProperty("description", description);
        embed.addProperty("color", Integer.parseInt(color.replace("#", ""), 16));

        JsonArray fieldArray = new JsonArray();
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            JsonObject field = new JsonObject();
            field.addProperty("name", entry.getKey());
            field.addProperty("value", entry.getValue());
            field.addProperty("inline", inline);
            fieldArray.add(field);
        }

        embed.add("fields", fieldArray);

        return embed;

    }
}
