package com.wolfco.velocity.managers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.JsonObject;
import com.velocitypowered.api.proxy.Player;
import com.wolfco.velocity.utils;

public class WebhookManager {
    static final String WEBHOOKURL = "https://discord.com/api/webhooks/1124457782993760307/Mr51SP6lxfaFYyS7o3yDySCPegK2RURfkfSSSxbhR2Kg5dUyWnuMi6C7ngo7wyNNtMNu";
    
    static public void sendMessage(Player player, String message) {
        sendMessage(player, message, true);
    }

    static public void sendMessage(Player player, String message, boolean useFilter) {

        if (useFilter) {
            message = utils.filterMessage(message);
        }

        try {
            String userName = player.getUsername();
            String avatarURL = "https://crafthead.net/helm/" + player.getUniqueId();

            JsonObject json = new JsonObject();

            json.addProperty("content", message);
            json.addProperty("username", userName);
            json.addProperty("avatar_url", avatarURL);

            URL url;

            try {
                url = new URI(WEBHOOKURL).toURL();
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
}
