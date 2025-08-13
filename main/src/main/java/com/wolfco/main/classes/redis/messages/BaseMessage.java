package com.wolfco.main.classes.redis.messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wolfco.main.Core;

public class BaseMessage {
    public String serverName;

    public BaseMessage() {}

    public String getServerName() {
        return serverName;
    }

    public String toJson() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            Core.get().log("Error converting message to JSON");
            return null;
        }
    }

    static public <T> T fromJson(String json, Class<T> clazz) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            Core.get().log("Error converting message to JSON");
            return null;
        }
    }
}
