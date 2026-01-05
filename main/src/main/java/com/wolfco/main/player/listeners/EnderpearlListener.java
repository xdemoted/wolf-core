package com.wolfco.main.player.listeners;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.wolfco.common.MessageUtility;
import com.wolfco.common.Utilities;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

@Named("EnderpearlListener")
@Singleton
public class EnderpearlListener {
    public static Boolean isAllowedBlock(Block block) {
        return block.isPassable() && block.getType() != Material.LADDER
                && block.getType() != Material.VINE
                && block.getType() != Material.WEEPING_VINES
                && block.getType() != Material.TWISTING_VINES
                && block.getType() != Material.CAVE_VINES_PLANT;
    }

    @Inject
    MessageUtility messageUtility;

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        TeleportCause cause = event.getCause();

        Location from = event.getFrom();

        if (cause == TeleportCause.ENDER_PEARL) {
            Location to = event.getTo();

            Double blockX = to.getBlockX() + 0.5;
            Double blockZ = to.getBlockZ() + 0.5;

            if (to.getX() - blockX < 0) {
                to.setX(Math.max(to.getX(), blockX - 0.2));
            } else {
                to.setX(Math.min(to.getX(), blockX + 0.2));
            }

            if (to.getZ() - blockZ < 0) {
                to.setZ(Math.max(to.getZ(), blockZ - 0.2));
            } else {
                to.setZ(Math.min(to.getZ(), blockZ + 0.2));
            }

            Block block = from.getBlock();

            Block upperBlock = block.getRelative(0, 1, 0);
            if (block.getType().isSolid() && upperBlock.getType().isSolid()) {
                player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL));
                messageUtility.sendPreset(player, "enderpearl.blocked", List.of("from"));
                event.setCancelled(true);
                return;
            }

            block = to.getBlock();
            upperBlock = block.getRelative(0, 1, 0);

            if (block.getType().isSolid()) {
                Vector vector = Utilities.getUnitLocation(from, to).multiply(0.3);
                int iterations = 0;
                while ((!isAllowedBlock(to.getBlock())) && iterations < 11) {
                    to.subtract(vector);
                    iterations++;
                }
                if (iterations >= 11) {
                    player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL));
                    messageUtility.sendPreset(player, "enderpearl.blocked", List.of("to"));
                    event.setCancelled(true);
                    return;
                }
            }

            if (upperBlock.getType().isSolid()) {
                to.setY(Math.min(to.getY(), upperBlock.getY() - 1.1));
            }
            event.setTo(to);
        }
    }
}
