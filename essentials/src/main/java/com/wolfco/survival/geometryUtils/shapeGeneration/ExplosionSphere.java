package com.wolfco.survival.geometryUtils.shapeGeneration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.joml.Vector3d;
import org.joml.Vector3f;

import com.wolfco.common.Utilities;
import com.wolfco.survival.Core;
import com.wolfco.survival.geometryUtils.GeometricUtils;

public class ExplosionSphere {
    final Map<Double, List<Material>> materials = new LinkedHashMap<>();

    {
        materials.put(0.0, List.of(Material.RESIN_BLOCK, Material.RESIN_BRICKS));
        materials.put(0.15, List.of(Material.ORANGE_WOOL, Material.ORANGE_CONCRETE, Material.ORANGE_STAINED_GLASS));
        materials.put(0.25, List.of(Material.ORANGE_TERRACOTTA, Material.HONEYCOMB_BLOCK, Material.YELLOW_TERRACOTTA));
        materials.put(0.35, List.of(Material.BLACK_TERRACOTTA, Material.BLACK_CONCRETE, Material.BLACK_STAINED_GLASS));
        materials.put(0.4, List.of(Material.BLACK_STAINED_GLASS, Material.BLACK_CONCRETE));
        materials.put(0.45, List.of(Material.GRAY_STAINED_GLASS, Material.BLACK_STAINED_GLASS));
        materials.put(0.8, List.of(Material.GRAY_STAINED_GLASS, Material.LIGHT_GRAY_STAINED_GLASS));
    }

    final double startRadius, endRadius;
    final int ticks;
    List<BlockDisplay> blocks = new ArrayList<>();

    Location location;
    World world;

    public ExplosionSphere(Location location, double startRadius, double endRadius, int ticks) {
        this.startRadius = Math.round(startRadius * 10) / 10;
        this.endRadius = Math.round(endRadius * 10) / 10;
        this.ticks = ticks;
        world = location.getWorld();
        this.location = location;

        if (world == null) {
            throw new IllegalArgumentException("Location must have a world");
        }

        createBlocks();
    }

    private void createBlocks() {
        for (int i = 0; i < 400; i++) {
            BlockDisplay block = (BlockDisplay) world.spawnEntity(location,
                    EntityType.BLOCK_DISPLAY);
            Material material = Utilities.getRandomValue(materials.get(0d));

            block.setBlock(Bukkit.createBlockData(material));

            block.addScoreboardTag("tempEntity");

            blocks.add(block);
        }
    }

    public void startInterpolation() {
        double radiusStep = (endRadius - startRadius) / ticks;
        int maxIterations = (int) Math.ceil(endRadius / radiusStep);
        int[] i = { 0 };

        Core.get().log("Start: " + startRadius + " End: " + endRadius + " Step:   " + radiusStep);

        new BukkitRunnable() {
            @Override
            public void run() {

                if (i[0] >= maxIterations) {
                    cancel();
                    for (BlockDisplay block : blocks) {
                        block.remove();
                    }
                    return;
                }

                double currentRadius = startRadius + i[0] * radiusStep;
                double progress = i[0] / (double) maxIterations;

                Core.get().log("Current Radius: " + currentRadius + " Progress: " + progress);

                Vector3d[] points = GeometricUtils.createFibonacciDistribution(blocks.size(), currentRadius);

                List<Material> materialList = List.of(Material.AIR);

                for (Double key : materials.keySet()) {

                    if (i[0] == 0) { // Debugging
                        Core.get().log("Key: " + key);
                        Core.get().log("Progress: " + progress);
                        Core.get().log("Key <= Progress: " + (key <= progress));
                    }

                    if (key <= progress) {
                        materialList = materials.get(key);
                    } else {
                        break;
                    }
                }

                String output = "Materials: ";
                for (Material material : materialList) {
                    output += material.name() + " ";
                }

                Core.get().log(output);

                for (int i = 0; i < blocks.size(); i++) {
                    BlockDisplay block = blocks.get(i);

                    if (Math.random() < 0.25 && !materialList.contains(block.getBlock().getMaterial())) {
                        Material material = Utilities.getRandomValue(materialList);

                        if (material == Material.AIR ) {
                            block.remove();
                            blocks.remove(i);
                            continue;
                        }

                        block.setBlock(Bukkit.createBlockData(material));
                    }

                    Transformation transformation = block.getTransformation();
                    block.setTransformation(new Transformation(
                            new Vector3f((float) -currentRadius / 6, (float) -currentRadius / 6,
                                    (float) -currentRadius / 6)
                                    .add((float) points[i].x, (float) points[i].y, (float) points[i].z),
                            transformation.getLeftRotation(),
                            new Vector3f((float) currentRadius / 3, (float) currentRadius / 3,
                                    (float) currentRadius / 3),
                            transformation.getRightRotation()));
                }

                i[0]++;
            }
        }.runTaskTimer(Core.get(), 0, 1);
    }
}
