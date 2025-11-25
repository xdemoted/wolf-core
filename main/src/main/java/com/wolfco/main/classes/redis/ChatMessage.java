package com.wolfco.main.classes.redis;

import org.json.JSONObject;

public class ChatMessage extends BaseMessage {
    Boolean colorEnabled = false;
    String messageData;

    public ChatMessage(String serverName, String formatData, String messageData) {
        super(serverName, formatData);
        this.messageData = messageData;
    }

    public String getMessageData() {
        return messageData;
    }

    public String getFormatData() {
        return data;
    }

    public Boolean getColorEnabled() {
        return colorEnabled;
    }

    public void enableColor() {
        this.colorEnabled = true;
    }

    @Override
    public String toJson() {
        String baseJson = super.toJson();
        JSONObject json = new JSONObject(baseJson);
        json.put("messageData", messageData);
        json.put("colorEnabled", colorEnabled);
        return json.toString();
    }

    public static ChatMessage fromJson(String jsonString) {
        JSONObject json = new JSONObject(jsonString);
        String serverName = json.getString("serverName");
        String formatData = json.getString("data");
        String messageData = json.getString("messageData");
        Boolean colorEnabled = json.getBoolean("colorEnabled");

        ChatMessage message = new ChatMessage(serverName, formatData, messageData);
        message.colorEnabled = colorEnabled;

        return message;
    }
}
