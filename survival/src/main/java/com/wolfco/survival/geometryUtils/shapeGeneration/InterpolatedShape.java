package com.wolfco.survival.geometryUtils.shapeGeneration;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;

abstract class InterpolatedShape {
    protected final float startRadius, endRadius;
    protected final int ticks;

    protected final Location center;
    protected final World world;

    protected final List<BlockDisplay> blocks = new ArrayList<>();

    public InterpolatedShape(Location center, float startRadius, float endRadius, int ticks) {
        this.startRadius = startRadius;
        this.endRadius = endRadius;
        this.ticks = ticks;

        this.center = center;
        this.world = center.getWorld();

        if (world == null) {
            throw new IllegalArgumentException("Location must have a world");
        }
    }

    public void createBlocks(int count, Material material) {
        for (int i = 0; i < 200; i++) {
            BlockDisplay block = (BlockDisplay) world.spawnEntity(center,
                    EntityType.BLOCK_DISPLAY);

            block.setBlock(Bukkit.createBlockData(Material.CALCITE));

            block.addScoreboardTag("tempEntity");

            blocks.add(block);
        }
    }

    @SuppressWarnings("unused")
    abstract void startInterpolation();
}
