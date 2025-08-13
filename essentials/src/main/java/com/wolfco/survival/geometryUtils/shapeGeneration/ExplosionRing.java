package com.wolfco.survival.geometryUtils.shapeGeneration;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.joml.Vector3d;
import org.joml.Vector3f;

import com.wolfco.survival.Core;
import com.wolfco.survival.geometryUtils.GeometricUtils;

public class ExplosionRing extends InterpolatedShape {
    final Map<Float, Material> materials = Map.of(
            0.0f, Material.SNOW_BLOCK,
            0.2f, Material.WHITE_CONCRETE_POWDER,
            0.4f, Material.CALCITE,
            0.5f, Material.DIORITE,
            0.6f, Material.WHITE_STAINED_GLASS,
            0.9f, Material.AIR);

    Location location;

    public ExplosionRing(Location location, float startRadius, float endRadius, int ticks) {
        super(location, startRadius, endRadius, ticks);

        this.location = location;

        if (world == null) {
            throw new IllegalArgumentException("Location must have a world");
        }

        createBlocks(100, Material.STONE);
    }

    @Override
    public void startInterpolation() {
        float[] currentRadius = { startRadius };
        float radiusStep = (endRadius - startRadius) / ticks;

        new BukkitRunnable() {
            @Override
            public void run() {

                if (currentRadius[0] >= endRadius) {
                    cancel();
                    for (BlockDisplay block : blocks) {
                        block.remove();
                    }
                    return;
                }

                currentRadius[0] += radiusStep;
                Vector3d[] points = GeometricUtils.createFibonacciDistribution(200, currentRadius[0]);
                float progress = (currentRadius[0] - startRadius) / (endRadius - startRadius);
                for (int i = 0; i < blocks.size(); i++) {
                    BlockDisplay block = blocks.get(i);
                    Material material = block.getBlock().getMaterial();

                    for (Float key : materials.keySet()) {
                        if (progress > key) {
                            material = materials.get(key);
                            break;
                        }
                    }

                    if (Math.random() < 0.25) {
                        if (material == Material.AIR) {
                            block.remove();
                            blocks.remove(i);
                            continue;
                        }

                        block.setBlock(Bukkit.createBlockData(material));
                    }
                    Transformation transformation = block.getTransformation();
                    block.setTransformation(new Transformation(
                            new Vector3f(-currentRadius[0] / 6, -currentRadius[0] / 6, -currentRadius[0] / 6)
                                    .add((float) points[i].x, (float) points[i].y, (float) points[i].z),
                            transformation.getLeftRotation(),
                            new Vector3f(currentRadius[0] / 3, currentRadius[0] / 3, currentRadius[0] / 3),
                            transformation.getRightRotation()));
                }
            }
        }.runTaskTimer(Core.get(), 0, 1);
    }
}
