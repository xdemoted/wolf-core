package com.wolfco.main.classes.redis;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.json.JSONObject;

public class ChatMessage extends BaseMessage {
    String name;
    UUID uuid;

    public ChatMessage(String serverName, Player player, String message) {
        super(serverName, message);
        this.uuid = player.getUniqueId();
        this.name = player.getName();
    }

    public ChatMessage(String serverName, String message, UUID uuid, String name) {
        super(serverName, message);
        this.uuid = uuid;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getMessage() {
        return data;
    }

    @Override
    public String toJson() {
        String baseJson = super.toJson();
        JSONObject json = new JSONObject(baseJson);
        json.put("uuid", uuid.toString());
        json.put("name", name);
        return json.toString();
    }

    public static ChatMessage fromJson(String jsonString) {
        JSONObject json = new JSONObject(jsonString);
        String serverName = json.getString("serverName");
        String message = json.getString("data");
        UUID uuid = UUID.fromString(json.getString("uuid"));
        String name = json.getString("name");

        return new ChatMessage(serverName, message, uuid, name);
    }
}
