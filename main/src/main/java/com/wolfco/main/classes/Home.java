package com.wolfco.main.classes;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World;

public class Home {
    public Home(String name, Location location) {
        this.name = name;
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
        
        World tempWorld = location.getWorld();
        try {
            if (tempWorld != null) {
                this.world = tempWorld.getUID();
            } else {
                this.world = null;
            }
        } catch (NullPointerException e) {
            this.world = null;
        }
    }

    public Home(String name, double x, double y, double z, float yaw, float pitch, UUID world) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.world = world;
    }
    
    public String name;
    public double x;
    public double y;
    public double z;
    public float yaw;
    public float pitch;
    public UUID world;
}
