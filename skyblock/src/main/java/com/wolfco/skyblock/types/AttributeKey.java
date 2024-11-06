package com.wolfco.skyblock.types;

import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;

public enum AttributeKey {
    DAMAGE(new NamespacedKey("wolfco","damage"),EquipmentSlotGroup.MAINHAND),
    ATTACK_SPEED(new NamespacedKey("wolfco","atkspeed"),EquipmentSlotGroup.MAINHAND),
    ARMOR(new NamespacedKey("wolfco","armor"),EquipmentSlotGroup.ARMOR),
    LEVEL(new NamespacedKey("wolfco","level"),null),
    TYPE(new NamespacedKey("wolfco","type"),null);

    public NamespacedKey key;
    public EquipmentSlotGroup slot;
    private AttributeKey(NamespacedKey key, EquipmentSlotGroup slot) {
        this.slot = slot;
        this.key = key;
    }

    public AttributeModifier getModifier(int level) {
        return new AttributeModifier(key, (double) level, AttributeModifier.Operation.ADD_NUMBER, slot);
    }
    public AttributeModifier getModifier(int level, WeaponType type) {
        if (this.name().equals("ATTACK_SPEED")) {
            return new AttributeModifier(key, -(4 - type.atkSpeedMultiplier * 1.6), AttributeModifier.Operation.ADD_NUMBER, slot);
        }
        return new AttributeModifier(key, (double) level, AttributeModifier.Operation.ADD_NUMBER, slot);
    }
}
