package com.wolfco.skyblock.handlers;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.plugin.Plugin;

import com.wolfco.skyblock.main;
import com.wolfco.skyblock.utils;

public class damageHandler implements Listener {
    Plugin plugin = main.getPlugin(main.class);
    
    @SuppressWarnings("deprecation")
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity damager = event.getDamager();
        plugin.getLogger().info("Dmg Event");
        if (damager instanceof Player) {
            // Player attacker = (Player) damager;
            double armorPoints = utils.getArmorValue(entity);
            utils.log("Armor Points: " + armorPoints);
            double damage = event.getDamage();
            utils.log("Damage: " + damage);
            double totalDamage = damage*(1-Math.max(armorPoints/5, armorPoints - damage/2)/25);
            utils.log("Total Damage: " + totalDamage);
            totalDamage = (totalDamage < 0) ? 0 : totalDamage;
            event.setDamage(DamageModifier.ARMOR, 0);
            event.setDamage(totalDamage);
        }
    }
}
