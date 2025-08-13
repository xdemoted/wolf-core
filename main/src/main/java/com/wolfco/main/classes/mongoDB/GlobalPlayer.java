package com.wolfco.main.classes.mongoDB;

import java.util.List;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import org.bukkit.entity.Player;

import com.wolfco.main.classes.mongoDB.subtypes.NamedLocation;
import com.wolfco.main.classes.mongoDB.subtypes.Session;

public class GlobalPlayer {
    @BsonProperty(value="_id")
    private ObjectId id;

    @BsonProperty(value="uuid_text")
    private String UUID;

    @BsonProperty(value="discord_id")
    private String discordID;

    private String nickname;

    private List<String> punishments;

    private List<Session> sessions;

    private List<String> usernames;

    private List<String> friends;

    private List<NamedLocation> homes;

    public GlobalPlayer() {
    }

    public GlobalPlayer(Player player) {
        this.UUID = player.getUniqueId().toString();
        this.discordID = "";
        this.nickname = "";
        this.punishments = List.of();
        this.sessions = List.of(new Session(player));
        this.usernames = List.of(player.getName());
        this.friends = List.of();
        this.homes = List.of();
    }

    public GlobalPlayer(GlobalPlayer player) {
        this.UUID = player.getUUID();
        this.discordID = player.getDiscordID();
        this.nickname = player.getNickname();
        this.punishments = player.getPunishments();
        this.sessions = player.getSessions();
        this.usernames = player.getUsernames();
        this.friends = player.getFriends();
        this.homes = player.getHomes();
    }

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

    public List<NamedLocation> getHomes() {
        return homes;
    }

    public GlobalPlayer setHomes(List<NamedLocation> homes) {
        this.homes = homes;
        return this;
    }
}