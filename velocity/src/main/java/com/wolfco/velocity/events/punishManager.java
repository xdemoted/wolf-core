package com.wolfco.velocity.events;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import com.wolfco.velocity.types.Punishment;

import dev.dejvokep.boostedyaml.YamlDocument;

public class punishManager {
    YamlDocument punishmentData;
    public punishManager(playerManager playerManager) {
        punishmentData = playerManager.punishmentData;
    }

    public HashMap<Integer, String> alphabet = new HashMap<>();
    {
        alphabet.put(0, "a");
        alphabet.put(1, "b");
        alphabet.put(2, "c");
        alphabet.put(3, "d");
        alphabet.put(4, "e");
        alphabet.put(5, "f");
        alphabet.put(6, "g");
        alphabet.put(7, "h");
        alphabet.put(8, "i");
        alphabet.put(9, "j");
        alphabet.put(10, "k");
        alphabet.put(11, "l");
        alphabet.put(12, "m");
        alphabet.put(13, "n");
        alphabet.put(14, "o");
        alphabet.put(15, "p");
        alphabet.put(16, "q");
        alphabet.put(17, "r");
        alphabet.put(18, "s");
        alphabet.put(19, "t");
        alphabet.put(20, "u");
        alphabet.put(21, "v");
        alphabet.put(22, "w");
        alphabet.put(23, "x");
        alphabet.put(24, "y");
        alphabet.put(25, "z");
    }
    public String createID() {
        String id = "";
        for (int i = 0; i < 5; i++) {
            Integer random = (int) Math.floor(Math.random() * 36);
            if (alphabet.containsKey(random)) {
                id += alphabet.get(random).toUpperCase();
            } else {
                id += (random - 26);
            }
        }
        if (punishmentData.contains(id)) {
            createID();
        }
        return id;
    }
    public Punishment logPunishment(UUID user, UUID punisher, String reason, String type, long endTime, String id) {
        return logPunishment(user, punisher.toString(), reason, type, endTime, id);
    }
    public Punishment logPunishment(UUID user, String name, String reason, String type, long endTime, String id) {
        Punishment punishment = new Punishment();
        punishment.id = id;
        punishmentData.set(punishment.id + ".user", user.toString());
        punishmentData.set(punishment.id + ".punisher", name);
        punishmentData.set(punishment.id + ".reason", reason);
        punishmentData.set(punishment.id + ".type", type);
        punishmentData.set(punishment.id + ".time", System.currentTimeMillis());
        punishmentData.set(punishment.id + ".endTime", endTime);
        try {
            punishmentData.save();
        } catch (IOException e) {}
        
        return punishment;
    }
    public void removePunishment(String id) {
        punishmentData.remove(id);
    }
    public Punishment getPunishment(String id) {
        Punishment punishment = new Punishment();
        punishment.id = id;
        punishment.user = punishmentData.getString(id + "user");
        punishment.punisher = punishmentData.getString(id + "punisher");
        punishment.reason = punishmentData.getString(id + "reason");
        punishment.type = punishmentData.getString(id + "type");
        punishment.time = punishmentData.getLong(id + "time");
        punishment.endTime = punishmentData.getLong(id + "endTime");
        return punishment;
    }
}
