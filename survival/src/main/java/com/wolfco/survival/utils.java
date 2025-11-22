package com.wolfco.survival;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Damageable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class utils {
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

    public static <T> void randomize(List<T> list) {
        if (list == null || list.size() <= 1)
            return;
        for (int i = list.size() - 1; i > 0; i--) {
            int j = java.util.concurrent.ThreadLocalRandom.current().nextInt(i + 1);
            T tmp = list.get(i);
            list.set(i, list.get(j));
            list.set(j, tmp);
        }
    }

    public static void randomizeListTowardsHeight(List<Block> list) {
        Random random = new Random();

        list.sort((a, b) -> {
            double scoreA = random.nextDouble()*a.getY() + a.getY(); // bias toward higher Y
            double scoreB = random.nextDouble()*b.getY() + b.getY();
            return Double.compare(scoreB, scoreA); // higher scores first
        });
    }

    public static void randomizeListAgainstHeight(List<Block> list) {
        Random random = new Random();

        list.sort((a, b) -> {
            double scoreA = random.nextDouble()*a.getY() - a.getY(); // bias toward higher Y
            double scoreB = random.nextDouble()*b.getY() - b.getY();
            return Double.compare(scoreB, scoreA); // higher scores first
        });
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

    public static void damageItem(ItemStack item, int damage) {
        if (item == null || item.getType().getMaxDurability() <= 0)
            return;

        ItemMeta meta = item.getItemMeta();
        if (!(meta instanceof Damageable))
            return;

        Damageable damageable = (Damageable) meta;
        int unbreakingLevel = item.getEnchantmentLevel(Enchantment.UNBREAKING);

        int appliedDamage = 0;

        for (int i = 0; i < damage; i++) {
            if (unbreakingLevel <= 0 || Math.random() < (1.0 / (unbreakingLevel + 1))) {
                appliedDamage++;
            }
        }

        damageable.setDamage(damageable.getDamage() + appliedDamage);
        item.setItemMeta(damageable);
    }
}
