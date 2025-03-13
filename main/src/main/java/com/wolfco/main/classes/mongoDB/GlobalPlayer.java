package com.wolfco.main.classes.mongoDB;

import java.util.UUID;

import org.json.JSONObject;

public class GlobalPlayer {
    public UUID uuid;
    public long lastLogin;
    public long lastLogout;
    
    public GlobalPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("uuid", uuid.toString());
        json.put("lastLogin", lastLogin);
        json.put("lastLogout", lastLogout);
        return json;
    }
}
