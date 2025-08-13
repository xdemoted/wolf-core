package com.wolfco.main.classes.mongoDB.subtypes;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.wolfco.common.Utilities;
import com.wolfco.main.Core;

public class NamedLocation {
    private String serverName;
    private String name;
    private String locationString;

    public NamedLocation() {
    }

    public NamedLocation(Location location, String serverName, String name) {
        this.serverName = serverName;
        this.name = name;
        this.locationString = Utilities.locationToString(location);
    }

    public String getServerName() {
        return serverName;
    }

    public NamedLocation setServerName(String serverName) {
        this.serverName = serverName;
        return this;
    }

    public String getName() {
        return name;
    }

    public NamedLocation setName(String name) {
        this.name = name;
        return this;
    }

    /*
    public static String locationToString(Location location) {
        return location.getWorld().getUID().toString() + "," +
                location.getX() + "," +
                location.getY() + "," +
                location.getZ() + "," +
                location.getYaw() + "," +
                location.getPitch();
    }
     */

    public String getLocationString() {
        return locationString;
    }

    public void setLocationString(String locationString) {
        this.locationString = locationString;
    }

    public void teleport(Player player) {
        Location location = Utilities.stringToLocation(locationString, Core.get().getServer());
        player.teleport(location);
    }
}
