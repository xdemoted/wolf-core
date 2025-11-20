package com.wolfco.survival;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class Harvest implements Listener {
    final static List<Material> hoes = new ArrayList<>();
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
            int[] damage = { 0 };
            utils.getBlocks(block.getWorld(), block.getLocation().subtract(1, 1, 1),
                    block.getLocation().add(1, 1, 1)).forEach(b -> {
                        if (harvest(b))
                            damage[0]++;
                    });
            Damageable meta = (Damageable) item.getItemMeta();
            meta.(meta.getDamage() + damage[0]);
            if (meta.getDamage() >= item.getType().getMaxDurability())
                player.getInventory().remove(item);
            item.setItemMeta((ItemMeta) meta);
        }
    }

    public boolean harvest(Block block) {
        Material productType = null;
        Material seedType;
        int minSeed = 1;
        int maxSeed = 4;

        switch (block.getType()) {
            case WHEAT -> {
                productType = Material.WHEAT;
                seedType = Material.WHEAT_SEEDS;
            }
            case CARROTS -> {
                seedType = Material.CARROT;
            }
            case POTATOES -> {
                seedType = Material.POTATO;
                minSeed = 2;
                maxSeed = 5;
            }
            case BEETROOTS -> {
                productType = Material.BEETROOT;
                seedType = Material.BEETROOT_SEEDS;
            }
            case COCOA -> {
                seedType = Material.COCOA_BEANS;
                minSeed = 3;
                maxSeed = 3;
            }
            case TORCHFLOWER_CROP -> {
                productType = Material.TORCHFLOWER;
                seedType = Material.TORCHFLOWER_SEEDS;
                maxSeed = 0;
            }
            case PITCHER_CROP -> {
                productType = Material.PITCHER_PLANT;
                seedType = Material.PITCHER_POD;
                maxSeed = 0;
            }
            default -> {
                return false;
            }
        }
        Ageable crop = (Ageable) block.getBlockData();
        if (crop.getAge() == crop.getMaximumAge()) {
            dropItem(block.getLocation(), seedType, minSeed, maxSeed);
            if (productType != null)
                dropItem(block.getLocation(), productType, 1);
            crop.setAge(0);
            block.setBlockData(crop);
        } else {
            return false;
        }

        return true;
    }

    public void dropItem(Location location, Material mat, int min, int max) {
        int rseeds = (int) Math.round(Math.random() * (max - min)) + min;
        dropItem(location, mat, rseeds);
    }

    public void dropItem(Location location, Material mat, int amount) {
        ItemStack itemStack = new ItemStack(mat, amount);
        location.getWorld().dropItemNaturally(location, itemStack);
    }
}
