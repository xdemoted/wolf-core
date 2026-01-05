package com.wolfco.survival;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import com.wolfco.common.classes.CorePlugin;

import jakarta.inject.Singleton;

@Singleton
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
    public void onStart() {
        this.getLogger().info("[Wolf-Core] Plugin enabled");
        getServer().getPluginManager().registerEvents(new Harvest(), this);

        clearTempEntities();
    }

    // This code is called before the server stops and before the /reload command
    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "{0}.onDisable()", this.getClass().getName());
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
}
