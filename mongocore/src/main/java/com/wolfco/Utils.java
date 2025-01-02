package com.wolfco;

import org.bukkit.Location;
import org.bukkit.World;

public class Utils {
    static public Location[] deserializeStringLocations(String serialized) {
        String[] parts = serialized.split(";");
        Location[] locations = new Location[parts.length];

        for (String part : parts) {
            locations[parts.length] = deserializeLocation(part);
        }

        return locations;
    }

    static public String serializeLocation(Location location) {
        if (!location.isWorldLoaded())
            return null;

        World world = location.getWorld();

        if (world == null)
            return null;

        return String.format("%s,%.2f,%.2f,%.2f;", world.getName(), location.getX(), location.getY(), location.getZ());
    }

    static public Location deserializeLocation(String serialized) {
        String[] parts = serialized.split(",");
        
        if (parts.length != 4)
            return null;

        World world = org.bukkit.Bukkit.getWorld(parts[0]);

        if (world == null)
            return null;

        return new Location(world, Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
    }
}
