package com.wolfco.survival;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;

public class utils {
    public static List<EntityType> allowedLeashableMobs = List.of(
            EntityType.ALLAY,
            EntityType.ARMADILLO,
            EntityType.AXOLOTL,
            EntityType.BAT,
            EntityType.BEE,
            EntityType.BLAZE,
            EntityType.OAK_BOAT,
            EntityType.OAK_CHEST_BOAT,
            EntityType.ACACIA_BOAT,
            EntityType.ACACIA_CHEST_BOAT,
            EntityType.BIRCH_BOAT,
            EntityType.BIRCH_CHEST_BOAT,
            EntityType.DARK_OAK_BOAT,
            EntityType.DARK_OAK_CHEST_BOAT,
            EntityType.JUNGLE_BOAT,
            EntityType.JUNGLE_CHEST_BOAT,
            EntityType.MANGROVE_BOAT,
            EntityType.MANGROVE_CHEST_BOAT,
            EntityType.SPRUCE_BOAT,
            EntityType.SPRUCE_CHEST_BOAT,
            EntityType.BAMBOO_RAFT,
            EntityType.BAMBOO_CHEST_RAFT,
            EntityType.BOGGED,
            EntityType.BREEZE,
            EntityType.CAT,
            EntityType.CAMEL,
            EntityType.CAVE_SPIDER,
            EntityType.MINECART,
            EntityType.CHEST_MINECART,
            EntityType.FURNACE_MINECART,
            EntityType.HOPPER_MINECART,
            EntityType.TNT_MINECART,
            EntityType.COD,
            EntityType.COW,
            EntityType.CREEPER,
            EntityType.DONKEY,
            EntityType.DOLPHIN,
            EntityType.DROWNED,
            EntityType.ELDER_GUARDIAN,
            EntityType.ENDER_DRAGON,
            EntityType.ENDERMAN,
            EntityType.ENDERMITE,
            EntityType.EVOKER,
            EntityType.FOX,
            EntityType.FROG,
            EntityType.GHAST,
            EntityType.GOAT,
            EntityType.GUARDIAN,
            EntityType.HOGLIN,
            EntityType.HUSK,
            EntityType.ILLUSIONER,
            EntityType.IRON_GOLEM,
            EntityType.LLAMA,
            EntityType.MAGMA_CUBE,
            EntityType.MOOSHROOM,
            EntityType.PANDA,
            EntityType.PHANTOM,
            EntityType.PIGLIN,
            EntityType.PIGLIN_BRUTE,
            EntityType.PILLAGER,
            EntityType.POLAR_BEAR,
            EntityType.RAVAGER,
            EntityType.SHULKER,
            EntityType.SILVERFISH,
            EntityType.SKELETON,
            EntityType.SKELETON_HORSE,
            EntityType.SLIME,
            EntityType.SNIFFER,
            EntityType.SNOW_GOLEM,
            EntityType.SPIDER,
            EntityType.SQUID,
            EntityType.STRAY,
            EntityType.STRIDER,
            EntityType.TADPOLE,
            EntityType.TRADER_LLAMA,
            EntityType.TURTLE,
            EntityType.VEX,
            EntityType.VILLAGER,
            EntityType.VINDICATOR,
            EntityType.WANDERING_TRADER,
            EntityType.WITCH,
            EntityType.WITHER,
            EntityType.WITHER_SKELETON,
            EntityType.ZOGLIN,
            EntityType.ZOMBIE,
            EntityType.ZOMBIE_HORSE,
            EntityType.ZOMBIE_VILLAGER,
            EntityType.ZOMBIFIED_PIGLIN,
            EntityType.SHEEP,
            EntityType.PIG,
            EntityType.CHICKEN,
            EntityType.HORSE,
            EntityType.MULE,
            EntityType.WOLF,
            EntityType.OCELOT,
            EntityType.CAT,
            EntityType.RABBIT,
            EntityType.PARROT,
            EntityType.TROPICAL_FISH,
            EntityType.SALMON,
            EntityType.PUFFERFISH
        );
    public static List<Material> logsThatBurn = List.of(
            Material.OAK_LOG,
            Material.STRIPPED_OAK_LOG,
            Material.BIRCH_LOG,
            Material.STRIPPED_BIRCH_LOG,
            Material.SPRUCE_LOG,
            Material.STRIPPED_SPRUCE_LOG,
            Material.JUNGLE_LOG,
            Material.STRIPPED_JUNGLE_LOG,
            Material.ACACIA_LOG,
            Material.STRIPPED_ACACIA_LOG,
            Material.DARK_OAK_LOG,
            Material.STRIPPED_DARK_OAK_LOG,
            Material.MANGROVE_LOG,
            Material.STRIPPED_MANGROVE_LOG,
            Material.CHERRY_LOG,
            Material.STRIPPED_CHERRY_LOG);

    public static List<Material> leaves = List.of(
            Material.OAK_LEAVES,
            Material.BIRCH_LEAVES,
            Material.SPRUCE_LEAVES,
            Material.JUNGLE_LEAVES,
            Material.ACACIA_LEAVES,
            Material.DARK_OAK_LEAVES,
            Material.MANGROVE_LEAVES,
            Material.CHERRY_LEAVES);

    public static List<Material> hoes = List.of(
            Material.WOODEN_HOE,
            Material.STONE_HOE,
            Material.IRON_HOE,
            Material.GOLDEN_HOE,
            Material.DIAMOND_HOE,
            Material.NETHERITE_HOE);

    public static List<Block> getBlocks(World world, Location pos1, Location pos2) {
        List<Block> blocks = new ArrayList<>();
        int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());
        int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    blocks.add(world.getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }

    public static boolean isBlockInRegion(Location blockLocation, Location pos1, Location pos2) {
        int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());
        int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

        return blockLocation.getBlockX() >= minX && blockLocation.getBlockX() <= maxX &&
                blockLocation.getBlockY() >= minY && blockLocation.getBlockY() <= maxY &&
                blockLocation.getBlockZ() >= minZ && blockLocation.getBlockZ() <= maxZ;
    }

    public static List<Block> getAdjacentBlocks(Block block) {
        List<Block> blocks = new ArrayList<>();

        for (BlockFace face : BlockFace.values()) {
            Block relativeBlock = block.getRelative(face);
            blocks.add(relativeBlock);
        }

        return blocks;
    }

    public static boolean isBlockNearLog(Block block, Location... excludes) {
        List<Location> excludedBlocks = new ArrayList<>(Arrays.asList(excludes));
        Location loc = block.getLocation();
        var pos1 = loc.clone().subtract(6,6,6);
        var pos2 = loc.clone().add(6,6,6);

        boolean logFound = false;
        List<Block> blocks;

        List<Block> nextBlocks = new ArrayList<>();
        nextBlocks.add(block);

        do {
            blocks = new ArrayList<>(nextBlocks);
            nextBlocks = new ArrayList<>();

            for (Block hostBlock : blocks) {
                List<Block> adjacentBlocks = getAdjacentBlocks(hostBlock);

                for (Block adjacentBlock : adjacentBlocks) {
                    if (nextBlocks.contains(adjacentBlock) || blocks.contains(adjacentBlock) || excludedBlocks.contains(adjacentBlock.getLocation())) continue;

                    if (isBlockInRegion(adjacentBlock.getLocation(), pos1, pos2)) {
                        if (logsThatBurn.contains(adjacentBlock.getType())) {
                            logFound = true;
                            break;
                        } else if (leaves.contains(adjacentBlock.getType())) {
                            nextBlocks.add(adjacentBlock);
                        }
                    }
                }

                if (logFound) break;
                excludedBlocks.add(hostBlock.getLocation());
            }
        } while (!nextBlocks.isEmpty() && !logFound);

        return logFound;
    }

    public static boolean isBlockNearLogDebug(Block block, Location... excludes) {
        Map<Location, Material> originalBlocks = new HashMap<>();
        List<Location> excludedBlocks = new ArrayList<>(Arrays.asList(excludes));
        Location loc = block.getLocation();
        var pos1 = loc.clone().subtract(6,6,6);
        var pos2 = loc.clone().add(6,6,6);

        boolean logFound = false;
        List<Block> blocks;

        List<Block> nextBlocks = new ArrayList<>();
        nextBlocks.add(block);

        do {
            blocks = new ArrayList<>(nextBlocks);
            nextBlocks = new ArrayList<>();

            for (Block hostBlock : blocks) {
                List<Block> adjacentBlocks = getAdjacentBlocks(hostBlock);

                for (Block adjacentBlock : adjacentBlocks) {
                    if (nextBlocks.contains(adjacentBlock) || blocks.contains(adjacentBlock) || excludedBlocks.contains(adjacentBlock.getLocation())) continue;

                    if (isBlockInRegion(adjacentBlock.getLocation(), pos1, pos2)) {
                        if (logsThatBurn.contains(adjacentBlock.getType())) {
                            originalBlocks.put(adjacentBlock.getLocation(), adjacentBlock.getType());
                            adjacentBlock.setType(Material.BROWN_CONCRETE);
                            logFound = true;
                            break;
                        } else if (leaves.contains(adjacentBlock.getType())) {
                            originalBlocks.put(adjacentBlock.getLocation(), adjacentBlock.getType());
                            adjacentBlock.setType(Material.RED_CONCRETE);
                            nextBlocks.add(adjacentBlock);
                        }
                    }
                }

                if (logFound) break;
                hostBlock.setType(Material.BLACK_STAINED_GLASS);
                excludedBlocks.add(hostBlock.getLocation());
            }
        } while (!nextBlocks.isEmpty() && !logFound);

        Bukkit.getScheduler().runTaskLater(Core.get(), () -> {
            for (Map.Entry<Location, Material> entry : originalBlocks.entrySet()) {
                Block blockToRestore = entry.getKey().getBlock();
                blockToRestore.setType(entry.getValue());
            }
        }, 100L); // Restore blocks after 1 second (20 ticks)

        return logFound;
    }
}
