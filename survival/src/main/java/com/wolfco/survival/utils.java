package com.wolfco.survival;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class utils {
    public static List<Block> getBlocks(World world, Location pos1, Location pos2) {
        List<Block> blocks = new ArrayList<Block>();
        for (int x = pos1.getBlockX(); x <= pos2.getBlockX(); x++) {
            for (int y = pos1.getBlockY(); y <= pos2.getBlockY(); y++) {
                for (int z = pos1.getBlockZ(); z <= pos2.getBlockZ(); z++) {
                    blocks.add(world.getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }

    public static List<Material> hoes = new ArrayList<Material>();
    static {
        hoes.add(Material.WOODEN_HOE);
        hoes.add(Material.STONE_HOE);
        hoes.add(Material.IRON_HOE);
        hoes.add(Material.GOLDEN_HOE);
        hoes.add(Material.DIAMOND_HOE);
        hoes.add(Material.NETHERITE_HOE);
    }

    public static Block[] getRelativeBlocks(Block block) {
        return new Block[] {
                block.getRelative(0, 1, 0),
                block.getRelative(0, -1, 0),
                block.getRelative(0, 0, 1),
                block.getRelative(0, 0, -1),
                block.getRelative(1, 0, 0),
                block.getRelative(-1, 0, 0)
        };
    }
}
