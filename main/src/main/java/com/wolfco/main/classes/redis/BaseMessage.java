package com.wolfco.main.classes.redis;

import org.json.JSONObject;

public class BaseMessage {
    public String serverName;
    public String data;

    public BaseMessage(String serverName, String data) {
        this.serverName = serverName;
        this.data = data;
    }

    public String toJson() {
        JSONObject json = new JSONObject();
        json.put("serverName", serverName);
        json.put("data", data);

        return json.toString();
    }

    public static BaseMessage fromJson(String jsonString) {
        JSONObject json = new JSONObject(jsonString);
        String serverName = json.getString("serverName");
        String data = json.getString("data");

        BaseMessage message = new BaseMessage(serverName, data);

        return message;
    }
}
