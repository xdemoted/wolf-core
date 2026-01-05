package com.wolfco.main.warps;

import java.util.UUID;

import org.bukkit.Location;

public class Warp {
    public String name;
    public double x;
    public double y;
    public double z;
    public UUID world;

    public static Warp fromLocation(Location location, String name) {
        Warp warp = new Warp();
        warp.name = name;
        warp.x = location.getX();
        warp.y = location.getY();
        warp.z = location.getZ();
        warp.world = location.getWorld().getUID();
        return warp;
    }
}
