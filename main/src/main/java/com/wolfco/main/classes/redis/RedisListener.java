package com.wolfco.main.classes.redis;

public interface RedisListener {
    void onSystemMessage(String message);

    void onPlayerJoin(String message);

    void onPlayerLeave(String message);

    void onPlayerSwitch(String message);
}