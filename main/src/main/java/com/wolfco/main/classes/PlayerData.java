package com.wolfco.main.classes;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.wolfco.common.Utilities;
import com.wolfco.main.Core;

import dev.dejvokep.boostedyaml.YamlDocument;
import net.kyori.adventure.text.Component;
import net.luckperms.api.model.user.User;

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

    public Location lastPosition;
    public Location logoutPosition;

    public UUID lastWorld = null;
    public UUID logoutWorld = null;

    public String ipaddress;
    public String username;

    public Map<String, Home> homes = new HashMap<>();

    public Map<Player, Request> pendingRequests = new LinkedHashMap<>();

    public timestamp timestamp = new timestamp();

    public PlayerData(Player host, YamlDocument data) {
        this(host, data, false);
    }

    public PlayerData(Player host, YamlDocument data, Boolean offline) {
        if (!offline) {
            this.host = host;
            this.username = host.getName();
            InetSocketAddress tempHost = host.getAddress();

            if (tempHost != null) {
                this.ipaddress = tempHost.getAddress().getHostAddress();
            } else {
                this.ipaddress = null;
            }
        }

        this.data = data;
        this.afk = false;
        this.teleportEnabled = true;
        this.godmode = false;
        this.muted = false;

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

        lastPosition = Utilities.loadLocation(data, "lastTeleport");

        logoutPosition = Utilities.loadLocation(data, "lastLocation");

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

    public Location getLogoutLocation() {
        return logoutPosition;
    }

    public Location getLastLocation() {
        return lastPosition;
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

        if (logoutPosition == null) {
            logoutPosition = host.getLocation();
        }

        data.set("lastPosition.x", logoutPosition.getX());
        data.set("lastPosition.y", logoutPosition.getY());
        data.set("lastPosition.z", logoutPosition.getZ());
        data.set("lastPosition.yaw", logoutPosition.getYaw());
        data.set("lastPosition.pitch", logoutPosition.getPitch());
        data.set("lastPosition.world", logoutPosition.getWorld().getUID().toString());

        if (lastPosition == null) {
            lastPosition = host.getLocation();
        }

        data.set("lastTeleport.x", logoutPosition.getX());
        data.set("lastTeleport.y", logoutPosition.getY());
        data.set("lastTeleport.z", logoutPosition.getZ());
        data.set("lastTeleport.yaw", logoutPosition.getYaw());
        data.set("lastTeleport.pitch", logoutPosition.getPitch());
        data.set("lastTeleport.world", logoutPosition.getWorld().getUID().toString());

        return data;
    }

    public Component getDisplayName() {
        User user = ((Core) Core.get()).getLuckPerms().getUserManager().getUser(host.getUniqueId());
        return  Utilities.getDisplayName(user);
    }

    public CompletableFuture<Component> getOfflineDisplayName() {
        CompletableFuture<User> user = ((Core) Core.get()).getLuckPerms().getUserManager().loadUser(host.getUniqueId());
        CompletableFuture<Component> displayNameFuture = new CompletableFuture<>();

        user.thenAcceptAsync(u -> {
            displayNameFuture.complete(Utilities.getDisplayName(u));
        });

        return displayNameFuture;
    }
}
