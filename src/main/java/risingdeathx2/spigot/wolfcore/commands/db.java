package risingdeathx2.spigot.wolfcore.commands;

import risingdeathx2.spigot.wolfcore.core;

public class db {
    public db(core core, String[] args) {
        String host = core.getConfig().getString("database.host");
        int port = core.getConfig().getInt("database.port");
        String user = core.getConfig().getString("database.user");
        String password = core.getConfig().getString("database.password");
        core.getLogger().info(password + " " + user + " " + port + " " + host);
    }
}
