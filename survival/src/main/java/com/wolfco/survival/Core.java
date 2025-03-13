package com.wolfco.survival;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.CorePlugin;
import com.wolfco.survival.commands.Raycast;
import com.wolfco.survival.commands.Resistance;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBase;

public class Core extends CorePlugin {
    final HashMap<String, Float> resistanceOverrides = new HashMap<>();
    {
        resistanceOverrides.put(Material.WATER.name(), 50.0f);
        resistanceOverrides.put(Material.LAVA.name(), 50.0f);
    }

    public Field resistanceField;
    public HashMap<String, Float> blockMap = new HashMap<>();

    // This code is called after the server starts and after the /reload command
    @Override
    public void onEnable() {
        this.getLogger().info("[Wolf-Core] Plugin enabled");

        clearTempEntities();

        getCommandLoader().registerAll(getCommands());

        mapBlockResistances();
    }

    // This code is called before the server stops and before the /reload command
    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "{0}.onDisable()", this.getClass().getName());
    }

    @Override
    public List<CoreCommandExecutor> getCommands() {
        List<CoreCommandExecutor> list = new ArrayList<>();
        list.add(new Raycast(this));
        list.add(new Resistance(this));
        return list;
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
        } catch (IllegalAccessException ex) {}

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
                } catch (IllegalArgumentException | NullPointerException | ExceptionInInitializerError | IllegalAccessException e) {
                    System.out.println("Exception triggered :(");
                }
            }
        }
    }
}
