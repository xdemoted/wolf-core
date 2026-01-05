package com.wolfco.common;

import org.bukkit.Location;

import dev.dejvokep.boostedyaml.YamlDocument;

public class DocumentUtils {
    private final YamlDocument document;

    public DocumentUtils(YamlDocument document) {
        this.document = document;
    }

    public Location saveLocation(String path, Location location) {
        if (location == null) {
            document.set(path, null);
            return null;
        }

        document.set(path + ".world", location.getWorld().getUID().toString());
        document.set(path + ".x", location.getX());
        document.set(path + ".y", location.getY());
        document.set(path + ".z", location.getZ());
        document.set(path + ".yaw", location.getYaw());
        document.set(path + ".pitch", location.getPitch());

        return location;
    }
}
