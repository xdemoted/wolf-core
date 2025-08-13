package com.wolfco.survival.geometryUtils.iterators;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

import com.wolfco.survival.Core;
import com.wolfco.survival.geometryUtils.IterableRay;

public class BlockUpdateIterator {
    private final Core core = (Core) Core.get();
    private int i = 0;

    private final IterableRay ray;
    private final List<Block> blocks;
    private double intensity;

    private final List<List<Block>> blockGroups = new ArrayList<>();

    public BlockUpdateIterator(IterableRay ray) {
        this.ray = ray;
        this.blocks = ray.getBlocks();
        this.intensity = ray.getIntensity();
    }

    public boolean hasNext() {
        return (i < blocks.size() && intensity > 0);
    }

    public List<Vector> getNextBlocks(List<Vector> exclusionList) { // To reduce memory usage, vectors were used instead of blocks
        Block block = blocks.get(i++);
        Block[] surrounding = new Block[] {
                block.getRelative(BlockFace.UP),
                block.getRelative(BlockFace.DOWN),
                block.getRelative(BlockFace.NORTH),
                block.getRelative(BlockFace.SOUTH),
                block.getRelative(BlockFace.EAST),
                block.getRelative(BlockFace.WEST),
        };

        List<Block> destroyedBlocks = new ArrayList<>();

        Float resistance = core.blockMap.get(block.getType().name());

        if (resistance == null)
            resistance = 0.0f;

        intensity -= (resistance + 0.3f) * 0.3f + 0.225f;

        for (Block b : surrounding) {
            if (block.getType().equals(Material.AIR) || exclusionList.contains(b.getLocation().toVector())) {
                continue;
            }
            
            resistance = core.blockMap.get(b.getType().name());

            if (resistance == null)
                resistance = 0.0f;

            if (intensity - (resistance + 0.3f) * 0.3f - 0.225f > 0) {
                destroyedBlocks.add(b);
            }
        }

        if (block.getType().equals(Material.AIR) || exclusionList.contains(block.getLocation().toVector())) {
            addBlockGroupIfNotEmpty(destroyedBlocks);
            return destroyedBlocks.stream().map(Block::getLocation).map(Location::toVector).toList();
        }

        if (intensity <= 0) {
            addBlockGroupIfNotEmpty(destroyedBlocks);

            ray.setBlockGroups(blockGroups);

            return destroyedBlocks.stream().map(Block::getLocation).map(Location::toVector).toList();
        }

        destroyedBlocks.add(block);

        addBlockGroupIfNotEmpty(destroyedBlocks);
        return destroyedBlocks.stream().map(Block::getLocation).map(Location::toVector).toList();
    }

    private void addBlockGroupIfNotEmpty(List<Block> blockGroup) {
        if (!blockGroup.isEmpty()) {
            blockGroups.add(blockGroup);
        }
    }
}
