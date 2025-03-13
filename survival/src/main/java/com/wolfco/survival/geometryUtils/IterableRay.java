package com.wolfco.survival.geometryUtils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import com.wolfco.survival.geometryUtils.iterators.BlockUpdateIterator;

public class IterableRay extends raycast {
    private List<List<Block>> blockGroups = new ArrayList<>();

    private final Location start;

    private double intensity = 0;

    public IterableRay(Location loc1, Location loc2, double intensity) {
        super(loc1, loc2);
        this.start = loc1;
        this.intensity = intensity; 
    }

    public BlockUpdateIterator getBlockUpdateIterator() {
        return new BlockUpdateIterator(this);
    }

    public double getDistance() {
        if (blockGroups.isEmpty()) {
            return 0;
        }

        return blockGroups.getLast().getLast().getLocation().distance(start);
    }


    public double getIntensity() {
        return intensity;
    }

    public List<List<Block>> getBlockGroups() {
        return blockGroups;
    }

    public void setBlockGroups(List<List<Block>> blockGroups) {
        this.blockGroups = blockGroups;
    }

    static public IterableRay createRay(Location loc1, double x, double y, double intensity) {
        x = toRadians(x);
        y = toRadians(y);

        Location loc2 = loc1.clone().add(
                Math.sin(x) * Math.sin(y + Math.PI / 2) * intensity * 2,
                Math.cos(y + Math.PI / 2) * (intensity) * 2,
                Math.cos(x) * Math.sin(y + Math.PI / 2) * intensity * 2);

        return new IterableRay(loc1, loc2, intensity);
    }

    static public IterableRay createRay(Location loc1, Location loc2, double magnitude, double intensity) {
        World world = loc1.getWorld();

        if (world == null)
            return null;

        loc2 = loc2.subtract(loc1).toVector().normalize().multiply(magnitude).toLocation(world);

        return new IterableRay(loc1, loc2, intensity);
    }

    static public IterableRay createRay(Location loc1, Location loc2, double intensity) {
        return new IterableRay(loc1, loc2, intensity);
    }
}
