package risingdeathx2.spigot.classkit;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class itemDictionary {
    public static List<String> loreList = Arrays.asList("§2Left click to choose class","§2Right click to preview class and abilities");
    public static NamespacedKey nameKey = new NamespacedKey(App.getPlugin(App.class), "menu");
    public static NamespacedKey menuKey = new NamespacedKey(App.getPlugin(App.class), "menuItem");
    public static Inventory createInventory(Player player,String title) {
        return Bukkit.createInventory(player, 27, title);
    }
    public static Inventory createMainInventory(Player player) {
        Inventory invent = createInventory(player, "Classes");
        invent.setItem(9,createClassItem("Miner", "miner", Material.STONE));
        return invent;
    }
    public static ItemStack createClassItem(String title, String menu,Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(title);
        itemMeta.setLore(loreList);
        itemMeta.getPersistentDataContainer().set(nameKey, PersistentDataType.STRING, menu);
        itemMeta.getPersistentDataContainer().set(menuKey, PersistentDataType.INTEGER, 1);
        item.setItemMeta(itemMeta);
        return item;
    }
    public static ItemStack createNonItem() {
        ItemStack item = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(" ");
        itemMeta.setLore(loreList);
        itemMeta.getPersistentDataContainer().set(menuKey, PersistentDataType.INTEGER, 1);
        item.setItemMeta(itemMeta);
        return item;
    }
}
