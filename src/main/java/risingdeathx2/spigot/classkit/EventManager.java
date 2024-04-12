package risingdeathx2.spigot.classkit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.luckperms.api.model.user.User;
import net.luckperms.api.util.Tristate;
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
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        Player player = event.getPlayer();
        User user = plugin.lp.getUserManager().getUser(player.getUniqueId());
        String prefix = user.getCachedData().getMetaData().getPrefix();
        String suffix = user.getCachedData().getMetaData().getSuffix();
        if (user.getCachedData().getPermissionData().checkPermission("wolf-co.chat.color").asBoolean()) {
            message = ChatColor.translateAlternateColorCodes('&', message);
        }
        String displayName = (prefix!=null ? prefix : "") + player.getName() + (suffix!=null ? suffix : "");
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', displayName) + " §8»§f " + message);

        // Send message across network
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Forward");
        out.writeUTF("ALL");
        out.writeUTF("chat");
        out.writeUTF(displayName + " §8»§f " + message);
        player.sendPluginMessage(plugin, "bungeecord:main", out.toByteArray());
        event.setCancelled(true);
    }
}