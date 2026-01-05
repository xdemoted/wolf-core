package com.wolfco.main.player.listeners;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.view.AnvilView;

import com.wolfco.common.listeners.CoreListener;
import com.wolfco.main.Core;

import jakarta.inject.Named;
import jakarta.inject.Singleton;

@Named("AnvilListener")
@Singleton
public class AnvilListener implements CoreListener {
    private final Core core;

    public AnvilListener(Core core) {
        this.core = core;
    }

    @EventHandler
    public void onAnvilUse(InventoryClickEvent event) {
        if (event.getWhoClicked().getGameMode() == GameMode.CREATIVE)
            return;

        if (event.getInventory().getType() == InventoryType.ANVIL) {
            if (event.getSlotType() != InventoryType.SlotType.RESULT)
                return;
            core.log("Anvil result slot clicked");
            if (event.getCurrentItem().getType().isAir())
                return;
            if (!event.getCursor().getType().isAir())
                return;

            AnvilView view = (AnvilView) event.getView();
            String text = view.getRenameText();

            if (text != null && text.contains("<")) {
                event.getWhoClicked().sendMessage("NAUGHTY NAUGHTY, NO MINIMESSAGE IN ANVILS");
                event.setCancelled(true);
            }
        }
    }
}
