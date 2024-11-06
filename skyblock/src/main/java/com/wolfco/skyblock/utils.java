package com.wolfco.skyblock;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.wolfco.skyblock.types.AttributeKey;
import com.wolfco.skyblock.types.WeaponType;

public class utils {
    // Data Lists
    public static List<Material> hoes = new ArrayList<Material>() {
        {
            add(Material.WOODEN_HOE);
            add(Material.STONE_HOE);
            add(Material.IRON_HOE);
            add(Material.GOLDEN_HOE);
            add(Material.DIAMOND_HOE);
            add(Material.NETHERITE_HOE);
        }
    };
    public static List<Material> swords = new ArrayList<Material>() {
        {
            add(Material.WOODEN_SWORD);
            add(Material.STONE_SWORD);
            add(Material.IRON_SWORD);
            add(Material.GOLDEN_SWORD);
            add(Material.DIAMOND_SWORD);
            add(Material.NETHERITE_SWORD);
        }
    };
    public static HashMap<Material, Integer> armorPoints = new HashMap<Material, Integer>() {
        {
            put(Material.LEATHER_HELMET, 1);
            put(Material.LEATHER_CHESTPLATE, 3);
            put(Material.LEATHER_LEGGINGS, 2);
            put(Material.LEATHER_BOOTS, 1);
            put(Material.CHAINMAIL_HELMET, 2);
            put(Material.CHAINMAIL_CHESTPLATE, 5);
            put(Material.CHAINMAIL_LEGGINGS, 4);
            put(Material.CHAINMAIL_BOOTS, 1);
            put(Material.IRON_HELMET, 2);
            put(Material.IRON_CHESTPLATE, 6);
            put(Material.IRON_LEGGINGS, 5);
            put(Material.IRON_BOOTS, 2);
            put(Material.GOLDEN_HELMET, 2);
            put(Material.GOLDEN_CHESTPLATE, 5);
            put(Material.GOLDEN_LEGGINGS, 3);
            put(Material.GOLDEN_BOOTS, 1);
            put(Material.DIAMOND_HELMET, 3);
            put(Material.DIAMOND_CHESTPLATE, 8);
            put(Material.DIAMOND_LEGGINGS, 6);
            put(Material.DIAMOND_BOOTS, 3);
            put(Material.NETHERITE_HELMET, 3);
            put(Material.NETHERITE_CHESTPLATE, 8);
            put(Material.NETHERITE_LEGGINGS, 6);
            put(Material.NETHERITE_BOOTS, 3);
        }
    };

    public static List<Block> getBlocks(World world, Location pos1, Location pos2) {
        List<Block> blocks = new ArrayList<Block>();
        int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());
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

    public static double random(double min, double max) {
        return Math.random() * (max - min) + min;
    }

    public static boolean itemDataCheck(ItemStack item) {
        return item != null && item.hasItemMeta() && item.getItemMeta().hasCustomModelData();
    }

    public static double getArmorPoints(ItemStack item) {
        if (!itemDataCheck(item)) {
            if (armorPoints.containsKey(item.getType())) {
                return armorPoints.get(item.getType());
            }
            return 0;
        }

        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        if (container.has(AttributeKey.ARMOR.key, PersistentDataType.DOUBLE)) {
            return container.get(AttributeKey.ARMOR.key, PersistentDataType.DOUBLE);
        } else {
            return 0;
        }
    }

    public static WeaponType getType(ItemStack item) {
        if (!itemDataCheck(item)) {
            return null;
        }

        WeaponType type = WeaponType.values()[item.getItemMeta().getCustomModelData()];

        if (type == null) {
            return null;
        }

        return type;
    }

    public static int getLevel(ItemStack item) {
        if (!itemDataCheck(item)) {
            return -1;
        }

        Integer level = item.getItemMeta().getPersistentDataContainer().get(AttributeKey.LEVEL.key,
                PersistentDataType.INTEGER);

        if (level == null) {
            return -1;
        }

        return level;
    }

    public static int getLevel(Player player) {
        EntityEquipment equipment = player.getEquipment();

        // Armor Level

        int armorLevel = Math.round(
                getLevel(equipment.getBoots()) * 0.15f +
                        getLevel(equipment.getLeggings()) * 0.3f +
                        getLevel(equipment.getChestplate()) * 0.4f +
                        getLevel(equipment.getHelmet()) * 0.15f);
        // Weapon Level

        int weaponLevel = 0;
        ItemStack mainHand = equipment.getItemInMainHand(), offHand = equipment.getItemInOffHand();
        if (swords.contains(mainHand.getType()) && itemDataCheck(offHand) && itemDataCheck(mainHand)) {
            WeaponType mainType = getType(mainHand);
            WeaponType offType = getType(offHand);
            if (mainType == WeaponType.CLAYMORE) {
                weaponLevel = getLevel(mainHand);
            } else if (mainType == WeaponType.DAGGER) {
                if (offType == WeaponType.DAGGER) {
                    weaponLevel = (int) Math.round((getLevel(mainHand) + getLevel(offHand)) / 2);
                } else {
                    weaponLevel = (int) Math.round(getLevel(mainHand) * 0.7);
                }
            } else {
                weaponLevel = getLevel(mainHand);
                int offLevel = getLevel(offHand);
                if (offLevel > weaponLevel) {
                    weaponLevel = offLevel;
                }
            }
        }
        main.getPlugin(main.class).getLogger().info("Armor Level: " + armorLevel);
        main.getPlugin(main.class).getLogger().info("Weapon Level: " + weaponLevel);
        return Math.round((armorLevel + weaponLevel) / 2.0f);
    }

    public static double getArmorValue(Entity entity) {
        try {
            Method getEquipment = entity.getClass().getMethod("getEquipment");
            EntityEquipment equipment = (EntityEquipment) getEquipment.invoke(entity);
            equipment.getArmorContents();
            ItemStack[] items = equipment.getArmorContents();
            double armorValue = 0;
            for (ItemStack item : items) {
                log("Armor Value: " + getArmorPoints(item));
                armorValue += getArmorPoints(item);
            }
            return armorValue;
        } catch (Exception e) {
            log("Error getting armor value");
        }
        return 0;
    }

    // Get Logger
    public static void log(String message) {
        main.getPlugin(main.class).getLogger().info(message);
    }
}
