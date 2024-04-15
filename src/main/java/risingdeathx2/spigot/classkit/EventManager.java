package risingdeathx2.spigot.classkit;

import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

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