package risingdeathx2.spigot.wolfcore.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import dev.dejvokep.boostedyaml.YamlDocument;

public class PlayerData {
    public class home {
        public String name;
        public double x;
        public double y;
        public double z;
        public float yaw;
        public float pitch;
        public UUID world;
    }

    public YamlDocument data;
    public Player host;

    public boolean afk;
    public boolean teleportEnabled;
    public boolean godmode; 
    public boolean muted;

    public String ipaddress;
    
    public Map<String,home> home = new HashMap<>();

    ArrayList<request> requests = new ArrayList<request>();
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
                home home = new home();
                home.name = key;
                home.x = data.getDouble("home." + key + ".x", 0.0);
                home.y = data.getDouble("home." + key + ".y", 0.0);
                home.z = data.getDouble("home." + key + ".z", 0.0);
                home.yaw = data.getFloat("home." + key + ".yaw", (float) 0.0);
                home.pitch = data.getFloat("home." + key + ".pitch", (float) 0.0);
                home.world = UUID.fromString(data.getString("home." + key + ".world"));
                this.home.put(key, home);
            }
        }
    }
    public request sendRequest(Player sender, String type) {
        // The host is the player sending the request, the target is this instance of the player
        request request = new request();
        request.host = sender;
        request.target = this.host;
        request.type = type;
        request.startTime = System.currentTimeMillis();
        requests.add(request);
        return request;
    }
    public boolean denyLastRequest() {
        if (requests.size() == 0) {
            return false;
        }
        requests.remove(requests.size() - 1);
        return true;
    }
    public request acceptLastRequest() {
        if (requests.size() == 0) {
            return null;
        }
        request request = requests.get(requests.size() - 1);
        if (System.currentTimeMillis() - request.startTime > 30000) {
            requests = new ArrayList<request>();
            return null;
        }
        requests.remove(requests.size() - 1);
        return request;
    }

}
