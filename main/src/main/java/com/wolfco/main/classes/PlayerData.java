package com.wolfco.main.classes;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
    public double[] lastPosition = new double[5]; // x, y, z, yaw, pitch
    public UUID lastWorld = null;

    public String ipaddress;
    public String username;

    public Map<String, Home> homes = new HashMap<>();

    public Map<Player, Request> pendingRequests = new LinkedHashMap<>();

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

        if (data.contains("lastTeleport")&&data.getString("lastTeleport.world")!=null) {
            this.lastWorld = UUID.fromString(data.getString("lastTeleport.world"));
            this.lastPosition[0] = data.getDouble("lastTeleport.x", 0.0);
            this.lastPosition[1] = data.getDouble("lastTeleport.y", 0.0);
            this.lastPosition[2] = data.getDouble("lastTeleport.z", 0.0);
            this.lastPosition[3] = data.getDouble("lastTeleport.yaw", 0.0);
            this.lastPosition[4] = data.getDouble("lastTeleport.pitch", 0.0);
        }

        if (data.contains("lastPosition")&&data.getString("lastPosition.world")!=null) {
            Location logoutLocation = new Location(
                host.getServer().getWorld(UUID.fromString(data.getString("lastPosition.world"))),
                data.getDouble("lastPosition.x", 0.0),
                data.getDouble("lastPosition.y", 0.0),
                data.getDouble("lastPosition.z", 0.0),
                data.getDouble("lastPosition.yaw", 0.0).floatValue(),
                data.getDouble("lastPosition.pitch", 0.0).floatValue()
            );
            host.teleport(logoutLocation);
        }

        timestamp.login = data.getLong("timestamp.login", (long) 0);
        timestamp.logout = data.getLong("timestamp.logout", (long) 0);
        timestamp.mute = data.getLong("timestamp.mute", (long) 0);
    }

    public Request sendRequest(Player sender, String type) {
        // The host is the player sending the request, the target is this instance of
        // the player
        Request request = new Request();
        request.type = type;
        request.startTime = System.currentTimeMillis();
        request.name = sender.getName();
        pendingRequests.put(sender, request);
        return request;
    }

    public Request getRequest(Player sender) {
        if (sender == null) {
            return pendingRequests.values().stream().findFirst().orElse(null);
        } else {
            return pendingRequests.get(sender);
        }
    }

    public Player getRequestSender(Request request) {
        for (Map.Entry<Player, Request> entry : pendingRequests.entrySet()) {
            if (entry.getValue() == request) {
                return entry.getKey();
            }
        }
        return null;
    }

    public boolean denyLastRequest() {
        return denyRequest(null);
    }

    public boolean denyRequest(Player sender) {
        Request targetRequest = getRequest(sender);

        if (targetRequest == null) {
            return false;
        }

        pendingRequests.remove(sender);

        return System.currentTimeMillis() - targetRequest.startTime <= 30000;
    }

    public Request acceptLastRequest() {
        return acceptRequest(null);
    }

    public Request acceptRequest(Player sender) {
        Request targetRequest = getRequest(sender);

        if (System.currentTimeMillis() - targetRequest.startTime > 30000) {
            pendingRequests.remove(sender);
            return null;
        }
        return targetRequest;
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
        Location logoutLocation = host.getLocation();

        if (logoutLocation == null) {
            logoutLocation = host.getWorld().getSpawnLocation();
        }

        data.set("lastPosition.x", logoutLocation.getX());
        data.set("lastPosition.y", logoutLocation.getY());
        data.set("lastPosition.z", logoutLocation.getZ());
        data.set("lastPosition.yaw", logoutLocation.getYaw());
        data.set("lastPosition.pitch", logoutLocation.getPitch());
        data.set("lastPosition.world", logoutLocation.getWorld().getUID().toString());

        data.set("lastTeleport.x", lastPosition[0]);
        data.set("lastTeleport.y", lastPosition[1]);
        data.set("lastTeleport.z", lastPosition[2]);
        data.set("lastTeleport.yaw", lastPosition[3]);
        data.set("lastTeleport.pitch", lastPosition[4]);
        data.set("lastTeleport.world", lastWorld != null ? lastWorld.toString() : null);

        return data;
    }
}
