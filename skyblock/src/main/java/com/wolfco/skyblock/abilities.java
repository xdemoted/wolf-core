package com.wolfco.skyblock;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class abilities {
    public static Plugin plugin = main.getPlugin(main.class);
    public static void meteoricStrike(Location target) {
        World world = target.getWorld();
        Location strikeTarget = target;
        List<Block> blocks = utils.getBlocks(world, target.subtract(new Vector(2,2,2)), target.add(new Vector(2,2,2)));
        for (Block block : blocks) {
            plugin.getLogger().info(block.getType().name());
            if (block.getType() == Material.AIR) {
                continue;
            }
            plugin.getLogger().info(String.valueOf(block.getType().isSolid()));
            if (block.getType().isSolid()&&block.getRelative(BlockFace.UP).getType() == Material.AIR) {
                strikeTarget = block.getLocation().add(0.5, 1, 0.5);
                break;
            }
        }

        ItemDisplay warning = (ItemDisplay) world.spawnEntity(strikeTarget.add(0, 0.5, 0), EntityType.ITEM_DISPLAY);
        ItemStack circle = new ItemStack(Material.RABBIT_HIDE);
        ItemMeta meta = circle.getItemMeta();
        meta.setCustomModelData(8);
        circle.setItemMeta(meta);
        warning.setItemStack(circle);
    }
    public static void meteorShower(Location target) {
        
    }
}
