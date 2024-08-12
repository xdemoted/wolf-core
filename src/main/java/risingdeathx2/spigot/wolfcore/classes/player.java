package risingdeathx2.spigot.wolfcore.classes;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class player {
    Player host;
    ArrayList<request> requests = new ArrayList<request>();
    public player(Player host) {
        this.host = host;
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
