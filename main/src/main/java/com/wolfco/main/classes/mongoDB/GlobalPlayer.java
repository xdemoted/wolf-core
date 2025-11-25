package com.wolfco.main.classes.mongoDB;

import java.util.UUID;

import org.json.JSONObject;

public class GlobalPlayer {
    public UUID uuid;
    public String nickname;
    public long lastLogin;
    public long lastLogout;

    public String discordID = "unset";
    
    public GlobalPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public String getUUID() {
        return uuid.toString();
    }

    public String getDiscordID() {
        return discordID;
    }

    public String getNickname() {
        return nickname;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("uuid", uuid.toString());
        json.put("lastLogin", lastLogin);
        json.put("lastLogout", lastLogout);
        return json;
    }
}
