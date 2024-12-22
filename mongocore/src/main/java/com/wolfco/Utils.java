package com.wolfco;

import org.bukkit.Location;
import org.bukkit.World;

public class Utils {
    static public String serializeLocation(Location location) {
        if (!location.isWorldLoaded())
            return null;

        World world = location.getWorld();

        if (world == null)
            return null;

        return String.format("%s,%.2f,%.2f,%.2f;", world.getName(), location.getX(), location.getY(), location.getZ());
    }
}
