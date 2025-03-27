package com.wolfco.main.classes.mongoDB.subtypes;

public class Session {
    private long login;
    private long logout;
    private String ip;

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
}
