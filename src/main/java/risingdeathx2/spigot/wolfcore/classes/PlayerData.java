package risingdeathx2.spigot.wolfcore.classes;

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

    public Map<String,Home> homes = new HashMap<>();

    public request lastRequest;
    public PlayerData(Player host, YamlDocument data) {
        this.host = host;
        this.data = data;
        this.afk = false;
        this.teleportEnabled = true;
        this.godmode = false;
        this.muted = false;
        this.ipaddress = host.getAddress().getAddress().getHostAddress();

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
        if (data.contains("timestamp")) {
            timestamp timestamp = new timestamp();
            timestamp.login = data.getLong("timestamp.login", (long) 0);
            timestamp.logout = data.getLong("timestamp.logout", (long) 0);
            timestamp.mute = data.getLong("timestamp.mute", (long) 0);
        }
    }
    public request sendRequest(Player sender, String type) {
        // The host is the player sending the request, the target is this instance of the player
        request request = new request();
        request.host = sender;
        request.type = type;
        request.startTime = System.currentTimeMillis();
        lastRequest = request;
        return lastRequest;
    }
    public void denyLastRequest() {
        lastRequest = null;
    }
    public request acceptLastRequest() {
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
        Location lastPosition = host.getLocation();
        data.set("lastPosition.x", lastPosition.getX());
        data.set("lastPosition.y", lastPosition.getY());
        data.set("lastPosition.z", lastPosition.getZ());
        return data;
    }
}
