package com.wolfco.velocity.types;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;

public class OfflinePlayer {
    public YamlDocument data;
    public HashMap<String, Object> changes = new HashMap<>();
    public final long login;
    public final long logout;
    public final String username;
    public final String ipaddress;
    public final List<Punishment> punishments;

    public OfflinePlayer(@Nonnull YamlDocument data) {
        this.data = data;
        this.login = login();
        this.logout = logout();
        this.username = username();
        this.ipaddress = ipaddress();
        this.punishments = punishments();
    }
    public void set(String key, Object value) {
        changes.put(key, value);
    }
    public void save() {
        for (String key : changes.keySet()) {
            data.set(key, changes.get(key));
        }
        try {
            data.save();
        } catch (IOException e) {
        }
        changes.clear();
    }

    // Retrieval Methods
    public long login() {
        return data.getLong("timestamp.login", (long) 0);
    }
    public long logout() {
        return data.getLong("timestamp.logout", (long) 0);
    }
    public String username() {
        return data.getString("username", "");
    }
    public String nickname() {
        return data.getString("nickname", username());
    }
    public String ipaddress() {
        return data.getString("ipaddress","");
    }
    public List<Punishment> punishments() {
        List<Punishment> punishments = new ArrayList<>();
        Section punishmentRoute = data.getSection("punishments");
        if (punishmentRoute == null) {
            return punishments;
        }
        Set<String> punishmentKeys = punishmentRoute.getRoutesAsStrings(false);
        for (String key : punishmentKeys) {
            Punishment punishment = new Punishment();
            punishment.id = key;
            punishment.endTime = punishmentRoute.getLong(key + ".endTime");
            punishment.reason = punishmentRoute.getString(key + ".reason");
            punishment.type = punishmentRoute.getString(key + ".type");
            punishments.add(punishment);
        }
        return punishments;
    }
    public Punishment isBanned() {
        for (Punishment punishment : punishments) {
            if (punishment.type.equals("ban")) {
                return punishment;
            }
        }
        return null;
    }

    // Setter Methods

    public void setLogin(long time) {
        set("timestamp.login", time);
    }
    public void setLogout(long time) {
        set("timestamp.logout", time);
    }
    public void setNickname(String nickname) {
        set("nickname", nickname);
    }
    public void setUsername(String name) {
        set("username", name);
    }
    public void setIP(String ip) {
        set("ipaddress", ip);
    }
    public void addPunishment(Punishment punishment) {
        set("punishments." + punishment.id + ".endTime", punishment.endTime);
        set("punishments." + punishment.id + ".reason", punishment.reason);
        set("punishments." + punishment.id + ".type", punishment.type);
    }
    public void removePunishment(String id) {
        data.remove("punishments." + id);
    }
}
