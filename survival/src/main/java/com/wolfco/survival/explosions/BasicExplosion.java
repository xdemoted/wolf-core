package com.wolfco.survival.explosions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;
import org.joml.Vector3d;

import com.wolfco.survival.Core;
import com.wolfco.survival.geometryUtils.GeometricUtils;
import com.wolfco.survival.geometryUtils.IterableRay;

public class BasicExplosion {
    List<IterableRay> rays = new ArrayList<>();
    List<List<Block>> blockGroups = new ArrayList<>();

    Location center;
    double intensity;

    public BasicExplosion(Location loc, int rayCount, double intensity) {
        this.center = loc;
        this.intensity = intensity;
        Core.get().log("Creating explosion at " + loc.toString());
        createRays(rayCount, intensity);
        Core.get().log("Created " + rays.size() + " rays");
        doPreCalculation();
    }

    public void start(double ticksPerStep) {
        World world = center.getWorld();

        if (world == null)
            return;

        doNextStep(ticksPerStep);
    }

    public void doNextStep(double ticksPerStep) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (doStep()) {
                    cancel();
                } else {
                    doNextStep(ticksPerStep);
                }
            }
        }.runTaskLater((Core) Core.get(), (int) Math.floor(ticksPerStep));
    }

    public boolean doStep() { // returns isFinished
        if (blockGroups.isEmpty())
            return true;

        List<Block> blockGroup = blockGroups.get(0);

        for (Block block : blockGroup) {
            block.setType(Material.AIR);
        }

        blockGroups.remove(0);

        return false;
    }

    public double getMaxDistance() {
        double maxDistance = 0;

        if (blockGroups.isEmpty())
            return 0;

        for (Block block : blockGroups.getLast()) {
            double distance = block.getLocation().distance(center);
            if (distance > maxDistance) {
                maxDistance = distance;
            }
        }

        return maxDistance;
    }

    private void createRays(int rayCount, double intensity) {
        Vector3d[] points = GeometricUtils.createFibonacciDistribution(rayCount, intensity);

        for (Vector3d point : points) {
            rays.add(IterableRay.createRay(center,
                    GeometricUtils.locationFromVector(point, center.getWorld()).add(center),
                    intensity + (Math.random() * 0.4 + 0.3)));
        }
    }

    private void doPreCalculation() {
        Core core = (Core) Core.get();

        for (IterableRay ray : rays) {
            double rayIntensity = ray.getIntensity();
            List<Block> blocks = ray.getBlocks();
            int i = 0;

            while (rayIntensity > 0&& i + 1 < blocks.size()) {
                Block block = blocks.get(i++);

                double resistance = (core.getResistance(block) + 0.3) * 0.3 + 0.225;
                if (Math.random() < 0.01) Core.get().log("Block " + block.getType().name() + " has resistance " + core.getResistance(block)); 
                rayIntensity -= resistance;

                List<Block> destroyedBlocks = new ArrayList<>();
                destroyedBlocks.add(block);

                Block[] surrounding = new Block[] {
                        block.getRelative(0, 1, 0),
                        block.getRelative(0, -1, 0),
                        block.getRelative(0, 0, 1),
                        block.getRelative(0, 0, -1),
                        block.getRelative(1, 0, 0),
                        block.getRelative(-1, 0, 0)
                };

                for (Block b : surrounding) {
                    if (b.getType().equals(Material.AIR)) {
                        continue;
                    }

                    resistance = core.getResistance(b);

                    if (rayIntensity - resistance > 0) {
                        destroyedBlocks.add(b);
                    }
                }

                this.appendBlockGroup(i, destroyedBlocks);
            }
        }
    }

    private void appendBlockGroup(int i, List<Block> blockGroup) {
        while (blockGroups.size() <= i) {
            blockGroups.add(new ArrayList<>());
        }

        blockGroups.get(i).addAll(blockGroup);
    }
}
