package com.wolfco.main.classes.mongoDB.subtypes;

import java.net.InetSocketAddress;

import org.bukkit.entity.Player;

import com.wolfco.common.Utilities;

public class Session {
    private long login;
    private long logout;
    private String ip;
    private String logoutLocationString;

    public Session() {
    }

    public Session(String ip) {
        this.login = System.currentTimeMillis();
        this.logout = 0;
        this.ip = ip;
    }

    public Session(Player player) {
        InetSocketAddress address = player.getAddress();

        this.login = System.currentTimeMillis();
        this.logout = 0;
        this.ip = (address != null) ? address.toString() : "unknown";
        this.logoutLocationString = Utilities.locationToString(player.getLocation());
    }

    public long getLogin() {
        return login;
    }

    public Session setLogin(long login) {
        this.login = login;
        return this;
    }

    public long getLogout() {
        return logout;
    }

    public Session setLogout(long logout) {
        this.logout = logout;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public Session setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getLogoutLocationString() {
        return logoutLocationString;
    }

    public Session setLogoutLocationString(String logoutLocationString) {
        this.logoutLocationString = logoutLocationString;
        return this;
    }
}
