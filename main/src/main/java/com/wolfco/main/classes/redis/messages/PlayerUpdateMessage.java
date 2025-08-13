package com.wolfco.main.classes.redis.messages;

import com.wolfco.main.classes.mongoDB.GlobalPlayer;

public class PlayerUpdateMessage extends BaseMessage {
    private String uuid;
    private String discordID;
    private String nickname;

    public PlayerUpdateMessage() {
        super();
    }

    public PlayerUpdateMessage(String serverName, GlobalPlayer player) {
        super();
        this.serverName = serverName;
        this.uuid = player.getUUID();
        this.discordID = player.getDiscordID();
        this.nickname = player.getNickname();
    }

    public PlayerUpdateMessage(String serverName, String uuid, String discordID, String nickname) {
        super();
        this.serverName = serverName;
        this.uuid = uuid;
        this.discordID = discordID;
        this.nickname = nickname;
    }

    public String getUuid() {
        return uuid;
    }

    public String getDiscordID() {
        return discordID;
    }

    public String getNickname() {
        return nickname;
    }
}
