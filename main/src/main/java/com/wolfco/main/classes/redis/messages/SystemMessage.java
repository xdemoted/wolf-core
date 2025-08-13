package com.wolfco.main.classes.redis.messages;

public class SystemMessage extends BaseMessage {
    private String message;

    public SystemMessage() {
        super();
    }

    public SystemMessage(String serverName, String message) {
        this.serverName = serverName;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
