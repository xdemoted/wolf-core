package com.wolfco.main.classes.redis.messages;

import com.wolfco.main.classes.mongoDB.GlobalPlayer;

public class PlayerSwitchMessage extends PlayerUpdateMessage {
    private String oldServerName;

    public PlayerSwitchMessage() {
        super();
    }

    public PlayerSwitchMessage(String serverName, String oldServerName, GlobalPlayer player) {
        super(serverName, player);
        this.serverName = serverName;
        this.oldServerName = oldServerName;
    }

    public PlayerSwitchMessage(String serverName, String oldServerName, String uuid, String discordID,
            String nickname) {
        super(serverName, uuid, discordID, nickname);
        this.serverName = serverName;
        this.oldServerName = oldServerName;
    }

    public String getOldServerName() {
        return oldServerName;
    }
}
