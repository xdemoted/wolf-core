package com.wolfco.survival;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class Harvest {
    public static List<Material> hoes = new ArrayList<Material>();
    static {
        hoes.add(Material.WOODEN_HOE);
        hoes.add(Material.STONE_HOE);
        hoes.add(Material.IRON_HOE);
        hoes.add(Material.GOLDEN_HOE);
        hoes.add(Material.DIAMOND_HOE);
        hoes.add(Material.NETHERITE_HOE);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            ItemStack item;
            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            ItemStack itemInOffHand = player.getInventory().getItemInOffHand();
            if (hoes.contains(itemInHand.getType()))
                item = itemInHand;
            else if (hoes.contains(itemInOffHand.getType()))
                item = itemInOffHand;
            else
                return;
            if (block.getType() == Material.WHEAT) {
                Ageable wheat = (Ageable) block.getBlockData();
                if (wheat.getAge() == wheat.getMaximumAge()) {
                    Damageable meta = (Damageable) item.getItemMeta();
                    meta.setDamage(meta.getDamage() + 1);
                    if (meta.getDamage() >= item.getType().getMaxDurability())
                        player.getInventory().remove(item);
                    item.setItemMeta((ItemMeta) meta);
                    utils.getBlocks(block.getWorld(), block.getLocation().subtract(-1, -1, -1),
                            block.getLocation().add(1, 1, 1)).forEach(this::harvestWheat);
                }
            }
        }
    }

    public void harvestWheat(Block block) {
        if (block.getType() != Material.WHEAT) {
            return;
        }
        Ageable wheat = (Ageable) block.getBlockData();
        if (wheat.getAge() == wheat.getMaximumAge()) {
            Integer rseeds = (int) Math.round(Math.random() * 3);
            ItemStack wheatStack = new ItemStack(Material.WHEAT);
            ItemStack seedsStack = new ItemStack(Material.WHEAT_SEEDS, rseeds);
            block.getWorld().dropItemNaturally(block.getLocation(), seedsStack);
            block.getWorld().dropItemNaturally(block.getLocation(), wheatStack);
            wheat.setAge(0);
            block.setBlockData(wheat);
        }
    }
}
