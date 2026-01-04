package com.wolfco.main.handlers;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.wolfco.common.listeners.CoreListener;
import com.wolfco.main.Core;

import jakarta.inject.Named;
import jakarta.inject.Singleton;
import net.kyori.adventure.text.minimessage.MiniMessage;

@Named("AnvilListener")
@Singleton
public class AnvilListener implements CoreListener {
    private final Core core;

    public AnvilListener(Core core) {
        this.core = core;
    }

    @EventHandler
    public void onAnvilUse(InventoryClickEvent event) {
        core.log(event.getInventory().getType().toString());
        core.log(event.getClick().name());
        core.log(event.getCurrentItem() != null ? event.getCurrentItem().getType().toString() : "null");
        core.log(event.getCursor().getType().toString());
        core.log(event.getSlotType().name());
        core.log("Slot: " + event.getSlot());

        if (event.getInventory().getType() == InventoryType.ANVIL) {
            if (event.getSlotType() != InventoryType.SlotType.RESULT)
                return;
            core.log("Anvil result slot clicked");
            if (event.getCurrentItem().getType().isAir())
                return;
            if (!event.getCursor().getType().isAir())
                return;
            Bukkit.getScheduler().runTask(core, () -> { // Run on next tick to avoid conflicts
                ItemStack item = event.getWhoClicked().getItemOnCursor();
                ItemMeta meta = item.getItemMeta();

                if (meta != null && meta.hasCustomName()) {
                    core.log("Renaming item...");
                    event.getWhoClicked().sendMessage(meta.customName());
                    event.getWhoClicked().sendMessage(MiniMessage.miniMessage().serialize(meta.customName()));
                    meta.customName(MiniMessage.miniMessage()
                            .deserialize(MiniMessage.miniMessage().serialize(meta.customName())));

                    item.setItemMeta(meta);

                    event.getWhoClicked().setItemOnCursor(item);
                }
            });
        }
    }
}
