package com.wolfco.main.warps;

import java.util.List;
import java.util.UUID;

import com.wolfco.main.Core;

import dev.dejvokep.boostedyaml.YamlDocument;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class WarpManager {
    YamlDocument warps;

    @Inject
    public WarpManager(Core core) {
        this.warps = core.getConfigDocument("warps.yml");
    }

    public YamlDocument getWarpDocument() {
        return warps;
    }

    public List<String> getWarps() {
        return warps.getRoutesAsStrings(false).stream().toList();
    }

    public Warp getWarp(String name) {
        if (!warps.contains(name)) return null;

        Warp warp = new Warp();

        warp.name = name;
        warp.world = UUID.fromString(warps.getString(name + ".world"));
        warp.x = warps.getDouble(name + ".x");
        warp.y = warps.getDouble(name + ".y");
        warp.z = warps.getDouble(name + ".z");
        return warp;
    }

    public boolean setWarp(Warp warp) {
        warps.set(warp.name + ".x", warp.x);
        warps.set(warp.name + ".y", warp.y);
        warps.set(warp.name + ".z", warp.z);
        warps.set(warp.name + ".world", warp.world.toString());

        try {
            warps.save();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean removeWarp(String name) {
        if (!warps.contains(name)) return false;

        warps.remove(name);

        try {
            warps.save();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
