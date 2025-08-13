package com.wolfco.survival;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.CorePlugin;
import com.wolfco.survival.commands.Findlog;
import com.wolfco.survival.commands.Raycast;
import com.wolfco.survival.commands.Resistance;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBase;

public class Core extends CorePlugin implements Listener {
    final List<Material> blockExclusionList = List.of(
            Material.CACTUS,
            Material.CHORUS_FLOWER,
            Material.FROSTED_ICE,
            Material.KELP,
            Material.MELON_STEM,
            Material.PUMPKIN_STEM,
            Material.SUGAR_CANE,
            Material.SWEET_BERRY_BUSH,
            Material.TORCHFLOWER_CROP,
            Material.TWISTING_VINES,
            Material.WEEPING_VINES);

    final HashMap<String, Float> resistanceOverrides = new HashMap<>();
    {
        resistanceOverrides.put(Material.WATER.name(), 50.0f);
        resistanceOverrides.put(Material.LAVA.name(), 50.0f);
    }

    public Field resistanceField;
    public HashMap<String, Float> blockMap = new HashMap<>();

    @Override
    public void onEnable() {
        this.getLogger().info("[Wolf-Core] Plugin enabled");
        getServer().getPluginManager().registerEvents(this, this);

        clearTempEntities();

        getCommandLoader().registerAll(getCommands());

        mapBlockResistances();
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "{0}.onDisable()", this.getClass().getName());
    }

    @Override
    public List<CoreCommandExecutor> getCommands() {
        List<CoreCommandExecutor> list = List.of(
                new Resistance(this),
                new Raycast(this),
                new Findlog(this));
        return list;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            org.bukkit.block.Block block = event.getClickedBlock();
            BlockData blockData = block.getBlockData();
            Material type = block.getType();
            if (blockData instanceof Ageable ageable && !blockExclusionList.contains(type)) {
                int age = ageable.getAge();
                int maxAge = ageable.getMaximumAge();

                if (age == maxAge) {
                    block.breakNaturally(event.getItem());
                    block.setType(type);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerBlockBreak(BlockBreakEvent event) {
        var block = event.getBlock();
        if (utils.logsThatBurn.contains(block.getType())) {
            Location loc = block.getLocation();
            Location pos1 = loc.clone().add(6, 6, 6);
            Location pos2 = loc.clone().subtract(6, 6, 6);

            var blocks = utils.getBlocks(block.getWorld(), pos1, pos2);

            ItemStack item = event.getPlayer().getInventory().getItemInOffHand();
            var itemMeta = item.getItemMeta();
            int unbreakingLevel = 0;

            if (!(itemMeta instanceof Damageable && item.getType() == Material.SHEARS)) {
                itemMeta = null;
            } else {
                unbreakingLevel = item.getEnchantmentLevel(Enchantment.UNBREAKING);
            }

            for (var b : blocks) {
                if (utils.leaves.contains(b.getType()) && !utils.isBlockNearLog(b, block.getLocation())) {
                    b.breakNaturally(item);

                    if (itemMeta != null) {
                        if (Math.random() < 1 / (unbreakingLevel + 1)) {
                            Damageable damageable = (Damageable) itemMeta;
                            int damage = damageable.getDamage() + 1;

                            if (damage >= item.getType().getMaxDurability()) {
                                item.setType(Material.AIR);
                                event.getPlayer().playSound(loc, Sound.ENTITY_ITEM_BREAK, 1, 1);
                                itemMeta = null;
                            } else {
                                damageable.setDamage(damage);
                            }
                        }
                    }
                }
            }

            if (itemMeta != null) {
                item.setItemMeta(itemMeta);
            }
        }
    }

    @EventHandler
    public void onPlayerEntityInteract(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        Player player = event.getPlayer();

        if (player.isSneaking()) {
            entity.getMetadata("Leash").forEach(meta -> {
                CorePlugin.get().log("Leash: " + meta.asString());
            });

            ItemStack mainHand = player.getInventory().getItemInMainHand();
            ItemStack offHand = player.getInventory().getItemInOffHand();
            ItemStack item;

            if (mainHand.getType() == Material.LEAD) {
                item = mainHand;
            } else if (offHand.getType() == Material.LEAD) {
                item = offHand;
            } else {
                return;
            }

            if (entity instanceof Villager || entity instanceof Player) {
                try {
                    ((Player) entity).getLeashHolder();
                } catch (IllegalStateException e) {
                    event.setCancelled(true);
                    item.setAmount(item.getAmount() - 1);
                    ((Player) entity).setLeashHolder(player);
                }
            }
        }
    }

    public void clearTempEntities() {
        List<World> worlds = Bukkit.getWorlds();

        for (World world : worlds) {
            for (Entity entity : world.getEntities()) {
                if (entity.getScoreboardTags().contains("tempEntity")) {
                    entity.remove();
                }
            }
        }
    }

    public double getResistance(org.bukkit.block.Block block) {
        Float resistance = blockMap.get(block.getType().name());

        if (resistance == null) {
            return 0.0;
        }

        return resistance;
    }

    private void mapBlockResistances() {
        List<Block> blocks = new ArrayList<>();

        try { // Make resistance method accessible
            for (Field field : Blocks.class.getFields()) {
                field.setAccessible(true);
                Block b = (Block) field.get(null);
                blocks.add(b);
            }
        } catch (IllegalAccessException ex) {
        }

        try { // Get resistance field
            resistanceField = BlockBase.class.getDeclaredField("aI");
            resistanceField.setAccessible(true);
        } catch (NoSuchFieldException ex) {
            System.out.println("Exception triggered :(");
        }

        if (resistanceField == null) {
            System.out.println("NO FIELD FOUND!");
        } else {
            for (Block block : blocks) {
                String blockName = block.v().split("\\.")[2].toUpperCase();

                if (resistanceOverrides.containsKey(blockName)) {
                    blockMap.put(blockName, resistanceOverrides.get(blockName));
                    continue;
                }

                try {
                    blockMap.put(blockName, resistanceField.getFloat(block));
                } catch (IllegalArgumentException | NullPointerException | ExceptionInInitializerError
                        | IllegalAccessException e) {
                    System.out.println("Exception triggered :(");
                }
            }
        }
    }
}
