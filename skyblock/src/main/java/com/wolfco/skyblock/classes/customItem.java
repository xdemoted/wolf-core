package com.wolfco.skyblock.classes;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.wolfco.skyblock.utils;
import com.wolfco.skyblock.types.ArmorType;
import com.wolfco.skyblock.types.AttributeKey;
import com.wolfco.skyblock.types.WeaponType;

public class customItem {
    private ItemStack item;
    public int level;
    public WeaponType type;

    public customItem(Material material, int level) {
        this.item = new ItemStack(material);
        this.level = level;
    }

    public customItem(ItemStack item) throws Exception {
        if (item == null || !item.hasItemMeta()) {
            throw new Exception("Invalid item");
        }
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasCustomModelData()) {
            throw new Exception("Invalid item");
        }
        this.item = item;
        this.type = utils.getType(item);
        this.level = item.getItemMeta().getPersistentDataContainer().get(AttributeKey.LEVEL.key,
                PersistentDataType.INTEGER);
        if (this.type == null || this.level == -1) {
            throw new Exception("Invalid item");
        }
    }

    public ItemStack addData(WeaponType type) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();

        meta.setCustomModelData(type.modelNumber);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, AttributeKey.DAMAGE.getModifier(level));
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, AttributeKey.ATTACK_SPEED.getModifier(level, type));
        container.set(AttributeKey.TYPE.key, PersistentDataType.STRING, AttributeKey.TYPE.name());
        container.set(AttributeKey.LEVEL.key, PersistentDataType.INTEGER, level);

        item.setItemMeta(meta);

        return getItem();
    }

    public ItemStack addData(ArmorType type) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();

        meta.setCustomModelData(type.modelNumber);
        container.set(AttributeKey.TYPE.key, PersistentDataType.STRING, AttributeKey.TYPE.name());
        container.set(AttributeKey.LEVEL.key, PersistentDataType.INTEGER, level);
        
        // Armor Points Caclulation
        double armorPoints = Math.min(-125 * (2 / level - 1), level / 2 - 25 * (2 / level - 1));
        String[] materialNameParts = item.getType().name().split("_");
        if (materialNameParts.length == 2) {
            switch (materialNameParts[1]) {
                case "HELMET":
                case "BOOTS":
                    armorPoints *= 0.15;
                    break;
                case "LEGGINGS":
                    armorPoints *= 0.3;
                    break;
                case "CHESTPLATE":
                    armorPoints *= 0.4;
                    break;
                default:
                    break;
            }
        }
        container.set(AttributeKey.ARMOR.key, PersistentDataType.DOUBLE, armorPoints);

        item.setItemMeta(meta);

        return getItem();
    }

    public ItemStack getItem() {
        return item;
    }
}
