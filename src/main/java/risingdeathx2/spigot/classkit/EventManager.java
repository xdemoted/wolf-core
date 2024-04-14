package risingdeathx2.spigot.classkit;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.luckperms.api.model.user.User;
import net.md_5.bungee.api.ChatColor;

/**
 * EventManager
 */
public class EventManager implements Listener {
    App plugin;

    public EventManager(App app) {
        plugin = app;
    }

    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle()=="Classes") {
            event.setCancelled(true);
        } else {
        }
    }
}