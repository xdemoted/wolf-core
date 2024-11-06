package com.wolfco.velocity.types;

import java.util.ArrayList;

public class ServerGroup {
    public String name;
    public ArrayList<String> players;
    public ServerGroup(String name) {
        this.name = name;
    }
    public void addPlayer(String name) {
        players.add(name);
    }
}