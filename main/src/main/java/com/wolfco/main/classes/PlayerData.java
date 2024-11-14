package com.wolfco.main.classes;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import dev.dejvokep.boostedyaml.YamlDocument;

public class PlayerData {
    public class timestamp {
        public long logout;
        public long login;
        public long mute;
    }

    public YamlDocument data;
    public Player host;

    public boolean afk;
    public boolean teleportEnabled;
    public boolean godmode;
    public boolean muted;

    public String ipaddress;
    public String username;

    public Map<String, Home> homes = new HashMap<>();

    public Request lastRequest;

    public timestamp timestamp = new timestamp();

    public PlayerData(Player host, YamlDocument data) {
        this.host = host;
        this.username = host.getName();
        this.data = data;
        this.afk = false;
        this.teleportEnabled = true;
        this.godmode = false;
        this.muted = false;

        InetSocketAddress tempHost = host.getAddress();
        
        if (tempHost != null) {
            this.ipaddress = tempHost.getAddress().getHostAddress();
        } else {
            this.ipaddress = null;
        }

        if (data.contains("home")) {
            for (String key : data.getSection("home").getRoutesAsStrings(false)) {
                double x = data.getDouble("home." + key + ".x", 0.0);
                double y = data.getDouble("home." + key + ".y", 0.0);
                double z = data.getDouble("home." + key + ".z", 0.0);
                float yaw = data.getFloat("home." + key + ".yaw", (float) 0.0);
                float pitch = data.getFloat("home." + key + ".pitch", (float) 0.0);
                UUID world = UUID.fromString(data.getString("home." + key + ".world"));
                Home home = new Home(key, x, y, z, yaw, pitch, world);
                this.homes.put(key, home);
            }
        }
        timestamp.login = data.getLong("timestamp.login", (long) 0);
        timestamp.logout = data.getLong("timestamp.logout", (long) 0);
        timestamp.mute = data.getLong("timestamp.mute", (long) 0);
    }

    public Request sendRequest(Player sender, String type) {
        // The host is the player sending the request, the target is this instance of
        // the player
        Request request = new Request();
        request.host = sender;
        request.type = type;
        request.startTime = System.currentTimeMillis();
        lastRequest = request;
        return lastRequest;
    }

    public boolean denyLastRequest() {
        if (System.currentTimeMillis() - lastRequest.startTime > 30000) {
            lastRequest = null;
            return false;
        } else {
            lastRequest = null;
            return true;
        }
    }

    public Request acceptLastRequest() {
        if (System.currentTimeMillis() - lastRequest.startTime > 30000) {
            lastRequest = null;
            return null;
        }
        return lastRequest;
    }

    public YamlDocument save() {
        for (String key : homes.keySet()) {
            Home home = homes.get(key);
            data.set("home." + key + ".x", home.x);
            data.set("home." + key + ".y", home.y);
            data.set("home." + key + ".z", home.z);
            data.set("home." + key + ".yaw", home.yaw);
            data.set("home." + key + ".pitch", home.pitch);
            data.set("home." + key + ".world", home.world.toString());
        }
        data.set("teleportEnabled", this.teleportEnabled);
        data.set("ipaddress", this.ipaddress);
        data.set("username", this.username);
        Location lastPosition = host.getLocation();

        if (lastPosition == null) {
            lastPosition = host.getWorld().getSpawnLocation();
        }

        data.set("lastPosition.x", lastPosition.getX());
        data.set("lastPosition.y", lastPosition.getY());
        data.set("lastPosition.z", lastPosition.getZ());
        
        return data;
    }
}
