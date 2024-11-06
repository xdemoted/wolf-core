package com.wolfco.skyblock;

import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class blockInfo {
    ItemDisplay display;
    public blockInfo(ItemDisplay display) {
        this.display = display;
    }
    public void remove() {
        display.remove();
    }
    public void setRotation(float x, float y) {
        display.setRotation(x, y);
    }
    public void setItemStack(ItemStack item) {
        display.setItemStack(item);
    }
    public ItemStack getItemStack() {
        return display.getItemStack();
    }
    public void getOutputs() {
        ItemStack item = display.getItemStack();
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer();
    }
}
