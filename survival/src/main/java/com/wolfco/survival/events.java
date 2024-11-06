package com.wolfco.survival;
import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.joml.Vector2d;

public class events implements Listener {
    public main plugin;
    public events (main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }
    @EventHandler
    public void blockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItem(event.getHand());
        ItemMeta meta = item.getItemMeta();
        if (meta.hasCustomModelData()) {
            ItemDisplay display = (ItemDisplay) player.getWorld().spawnEntity(event.getBlock().getLocation().add(0.5, 0.5, 0.5), EntityType.ITEM_DISPLAY);
            display.setItemStack(item);
            //Transformation trans = new Transformation(new Vector3f(0.5f,0.5f,0.5f), new AxisAngle4f(), new Vector3f(1,1,1), new AxisAngle4f());
            Vector2d direction = new Vector2d(player.getLocation().getDirection().getX(),player.getLocation().getDirection().getZ());
            direction.normalize();

            float yaw = (float) Math.toDegrees(Math.atan2(-direction.x, direction.y));
            float rotation = Math.round(yaw / 90) * 90;

            display.setRotation(rotation - 90, 0);
        }
    }
    @EventHandler
    public void breakBlock(BlockBreakEvent event) {
        Block block = event.getBlock();
        Collection<Entity> entities = block.getWorld().getNearbyEntities(block.getLocation().add(0.5, 0.5, 0.5), 0.1, 0.1, 0.1);
        ItemDisplay display = null;
        for (Entity entity : entities) {
            if (entity instanceof ItemDisplay) {
                display = (ItemDisplay) entity;
                break;
            }
        }
        if (display != null) {
            event.setCancelled(true);
            display.remove();
            ItemStack item = display.getItemStack();
            item.setAmount(1);
            block.getWorld().dropItemNaturally(block.getLocation().add(0.5, 0.5, 0.5), item);
            block.setType(Material.AIR);
        }
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            ItemStack item;
            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            ItemStack itemInOffHand = player.getInventory().getItemInOffHand();
            if (utils.hoes.contains(itemInHand.getType())) item = itemInHand;
            else if (utils.hoes.contains(itemInOffHand.getType())) item = itemInOffHand;
            else return;
            if (block.getType() == Material.WHEAT) {
                Ageable wheat = (Ageable) block.getBlockData();
                if (wheat.getAge() == wheat.getMaximumAge()) {
                    Damageable meta = (Damageable) item.getItemMeta();
                    meta.setDamage(meta.getDamage() + 1);
                    if (meta.getDamage() >= item.getType().getMaxDurability()) player.getInventory().remove(item);
                    item.setItemMeta((ItemMeta) meta);
                    utils.getBlocks(block.getWorld(), block.getLocation().add(-1, -1, -1), block.getLocation().add(1, 1, 1)).forEach(this::harvestWheat);
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
