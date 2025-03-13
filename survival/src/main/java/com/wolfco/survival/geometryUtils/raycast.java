package com.wolfco.survival.geometryUtils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import com.wolfco.survival.Core;

public class raycast {
    World world;
    double x0;
    double y0;
    double z0;
    double x1;
    double y1;
    double z1;

    public raycast(Location loc1, Location loc2) {
        if (loc1.getWorld() != loc2.getWorld()) {
            throw new IllegalArgumentException("Locations must be in the same world");
        }

        this.world = loc1.getWorld();
        this.x0 = loc1.getX();
        this.y0 = loc1.getY();
        this.z0 = loc1.getZ();
        this.x1 = loc2.getX();
        this.y1 = loc2.getY();
        this.z1 = loc2.getZ();
    }

    public Location lerp(double t) {
        double x = x0 + (x1 - x0) * t;
        double y = y0 + (y1 - y0) * t;
        double z = z0 + (z1 - z0) * t;
        return new Location(world, x, y, z);
    }

    final public List<Block> getBlocks() {
        double dx = x1 - x0;
        double dy = y1 - y0;
        double dz = z1 - z0;

        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
        int n = (int) Math.ceil(distance);

        List<Block> blocks = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            double t = i / (double) n;
            Location loc = lerp(t);
            blocks.add(loc.getBlock());
        }

        return blocks;
    }

    public void explode(float intensity) {
        List<Block> blocks = getBlocks();

        for (Block block : blocks) {
            Float resistance;

            try {
                resistance = ((Core) Core.get()).blockMap.get(block.getType().name());
            } catch (Exception e) {
                resistance = 0f;
            }

            intensity -= (resistance + 0.3f) * 0.3f;

            if (intensity > 0) {
                block.setType(org.bukkit.Material.AIR);
            }

            intensity -= 0.225f;

            if (intensity <= 0) {
                break;
            }
        }
    }

    static public raycast createRay(Location loc1, double x, double y, double Magnitude) {
        x = toRadians(x);
        y = toRadians(y);

        Location loc2 = loc1.clone().add(
                Math.sin(x) * Math.sin(y + Math.PI / 2) * Magnitude,
                Math.cos(y + Math.PI / 2) * Magnitude,
                Math.cos(x) * Math.sin(y + Math.PI / 2) * Magnitude);

        return new raycast(loc1, loc2);
    }

    static public double toRadians(double degrees) {
        return degrees * Math.PI / 180;
    }
}
