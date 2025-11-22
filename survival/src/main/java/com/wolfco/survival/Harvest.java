package com.wolfco.survival;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.spawner.BaseSpawner;

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
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (itemInHand.containsEnchantment(Enchantment.SILK_TOUCH) && block.getType() == Material.SPAWNER) {
            BaseSpawner blockData = (BaseSpawner) block.getState();
            EntityType spawnedType = blockData.getSpawnedType();
            if (blockData.getSpawnedType() == null)
                return;

            Material spawnEggMaterial = Material.getMaterial(spawnedType + "_SPAWN_EGG");

            if (spawnEggMaterial == null)
                return;

            ItemStack spawnerItem = new ItemStack(spawnEggMaterial, 1);
            block.getWorld().dropItemNaturally(block.getLocation(), spawnerItem);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        if (block.getType() == Material.SPAWNER) {
            BaseSpawner blockData = (BaseSpawner) block.getState();
            blockData.setSpawnRange(32);
        }
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        if (entity.getType() == EntityType.CREEPER) {
            if (entity.getCustomName() == null && Math.random() < 0.5) {
                createBabyCreeper((Creeper) entity);
                createBabyCreeper(entity.getLocation()).setCustomName("");
                createBabyCreeper(entity.getLocation()).setCustomName("");
                createBabyCreeper(entity.getLocation()).setCustomName("");
            }
        }
        /*else if (entity.getType() == EntityType.ENDERMAN) {
            Enderman enderman = (Enderman) entity;
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (enderman.isDead()) {
                        cancel();
                        return;
                    }
                    BlockData carriedBlock = enderman.getCarriedBlock();
                    if (carriedBlock != null && enderman.isValid()) {
                        if (Math.random() > 1)
                            return;
                        Location loc = enderman.getLocation();
                        List<Block> nearbyBlocks = utils.getBlocks(enderman.getWorld(),
                                enderman.getLocation().subtract(2, 5, 2),
                                enderman.getLocation().add(2, 5, 2));
                        utils.randomizeListTowardsHeight(nearbyBlocks);
                        for (Block b : nearbyBlocks) {
                            if (b.getType() != Material.AIR && b.getRelative(0, 1, 0).getType() == Material.AIR) {
                                b.getRelative(0, 1, 0).setType(carriedBlock.getMaterial());
                                enderman.setCarriedBlock(null);
                                if (Math.random() < 1) {
                                    enderman.teleport(b.getLocation().add(0, 2, 0));
                                }
                                break;
                            }
                        }
                    } else if (enderman.getCarriedBlock() == null && Math.random() < 1) {
                        List<Block> nearbyBlocks = utils.getBlocks(enderman.getWorld(),
                                enderman.getLocation().subtract(5, 5, 5),
                                enderman.getLocation().add(5, 5, 5));
                        utils.randomizeListAgainstHeight(nearbyBlocks);
                        for (Block block : nearbyBlocks) {
                            if (block.getType() != Material.AIR && block.getType().isSolid()) {
                                enderman.setCarriedBlock(block.getBlockData());
                                block.setType(Material.AIR);
                                break;
                            }
                        }
                    }
                };
            }.runTaskTimer((Core) Core.get(), 0L, 1L);
        }
            */
    }

    public Creeper createBabyCreeper(Location location) {
        if (location.getWorld() == null)
            return null;

        Creeper creeper = (Creeper) location.getWorld().spawn(location, Creeper.class, c -> {
            createBabyCreeper(c);
        });

        return creeper;
    }

    public Creeper createBabyCreeper(Creeper creeper) {
        creeper.setCustomName("a");
        creeper.getAttribute(Attribute.SCALE).setBaseValue(0.5);
        creeper.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.5);
        creeper.getAttribute(Attribute.MAX_HEALTH).setBaseValue(5);
        creeper.setExplosionRadius(1);
        return creeper;
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
            meta.setDamage(meta.getDamage() + damage[0]);
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
