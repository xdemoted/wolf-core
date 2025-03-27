package com.wolfco.main.classes.mongoDB;

import java.util.List;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import com.wolfco.main.classes.mongoDB.subtypes.Address;
import com.wolfco.main.classes.mongoDB.subtypes.Session;

public class GlobalPlayer {
    @BsonProperty(value="_id")
    private ObjectId id;

    @BsonProperty(value="uuid_text")
    private String UUID;

    @BsonProperty(value="discord_id")
    private String discordID;

    private String nickname;

    @BsonProperty(value="last_login")
    private long lastLogin;

    @BsonProperty(value="last_logout")
    private long lastLogout;

    private List<Address> addresses;

    private List<String> punishments;

    private List<Session> sessions;

    private List<String> usernames;

    private List<String> friends;

    public ObjectId getId() {
        return id;
    }

    public GlobalPlayer setId(ObjectId id) {
        this.id = id;
        return this;
    }

    public String getUUID() {
        return UUID;
    }

    public GlobalPlayer setUUID(String UUID) {
        this.UUID = UUID;
        return this;
    }

    public String getDiscordID() {
        return discordID;
    }

    public GlobalPlayer setDiscordID(String discordID) {
        this.discordID = discordID;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public GlobalPlayer setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public long getLastLogin() {
        return lastLogin;
    }

    public GlobalPlayer setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
        return this;
    }

    public long getLastLogout() {
        return lastLogout;
    }

    public GlobalPlayer setLastLogout(long lastLogout) {
        this.lastLogout = lastLogout;
        return this;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public GlobalPlayer setAddresses(List<Address> addresses) {
        this.addresses = addresses;
        return this;
    }

    public List<String> getPunishments() {
        return punishments;
    }

    public GlobalPlayer setPunishments(List<String> punishments) {
        this.punishments = punishments;
        return this;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public GlobalPlayer setSessions(List<Session> sessions) {
        this.sessions = sessions;
        return this;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public GlobalPlayer setUsernames(List<String> usernames) {
        this.usernames = usernames;
        return this;
    }

    public List<String> getFriends() {
        return friends;
    }

    public GlobalPlayer setFriends(List<String> friends) {
        this.friends = friends;
        return this;
    }
}
