package com.wolfco.skyblock.handlers;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Enemy;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

import com.wolfco.skyblock.main;
import com.wolfco.skyblock.utils;

public class entityHandler implements Listener {
    private HashMap<UUID, Entity> entities = new HashMap<UUID, Entity>();
    private main plugin;

    public entityHandler(main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(EntitySpawnEvent event) {
        Entity spawnedEntity = event.getEntity();
        Enemy enemy;
        Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();
        Player closestPlayer = null;
        int maxCustomEntities, baseHealth;
        double playerLevel, distance, closestDistance = Double.MAX_VALUE;
        AttributeInstance maxHealth;

        if (!(spawnedEntity instanceof Enemy)) {
            return;
        }

        maxCustomEntities = players.size() * 5;

        if (entities.size() < maxCustomEntities&&Math.random()<0.1) {
            enemy = (Enemy) spawnedEntity;
            try {
                for (Player player : players) {
                    distance = player.getLocation().distance(spawnedEntity.getLocation());
                    if (distance < closestDistance) {
                        closestDistance = distance;
                        closestPlayer = player;
                    }
                }

                if (closestPlayer != null) {
                    // Assignments
                    playerLevel = utils.getLevel(closestPlayer);
                    maxHealth = enemy.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                    baseHealth = (int) maxHealth.getBaseValue();
                    
                    // Entity Setup
                    maxHealth.setBaseValue(baseHealth * (baseHealth/20)*(playerLevel*Math.pow(2,playerLevel/50)));
                    enemy.setHealth(maxHealth.getBaseValue());
                    enemy.setCustomName("Level " + playerLevel);
                    enemy.setCustomNameVisible(true);
                }
            } catch (Exception e) {
                plugin.getLogger().info("Failed to set custom name for entity");
            }
            entities.put(spawnedEntity.getUniqueId(), spawnedEntity);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        entities.remove(entity.getUniqueId());
    }
}