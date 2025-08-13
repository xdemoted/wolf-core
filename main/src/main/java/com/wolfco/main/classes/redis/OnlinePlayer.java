package com.wolfco.main.classes.redis;

import com.wolfco.main.classes.mongoDB.GlobalPlayer;

public class OnlinePlayer extends GlobalPlayer {
    private String server;
    private boolean isAFK;

    public OnlinePlayer() {
        super();
    }

    public OnlinePlayer(GlobalPlayer player) {
        super(player);
    }

    public String getServer() {
        return server;
    }

    public OnlinePlayer setServer(String server) {
        this.server = server;
        return this;
    }

    public boolean isIsAFK() {
        return isAFK;
    }

    public void setIsAFK(boolean isAFK) {
        this.isAFK = isAFK;
    }
}
