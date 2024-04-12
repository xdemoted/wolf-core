package risingdeathx2.spigot.classkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Command implements CommandExecutor {
    App plugin;

    public Command(App app) {
        plugin = app;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String arg0, String[] arg1) {
        // Player player = sender.getServer().getPlayer(sender.getName());
        // sender.sendMessage(Arrays.toString(arg1));
        // if (arg1.length == 0) {
        //     sender.sendMessage("Empty");
        //     Inventory inventory = itemDictionary.createMainInventory(player);
        //     for (String key : plugin.getConfig().getConfigurationSection("classes").getKeys(false)) {
        //         ItemStack item = new ItemStack(
        //                 Material.valueOf(plugin.getConfig().getString("classes." + key + ".material", "BEDROCK")));
        //         ItemMeta meta = item.getItemMeta();
        //         meta.setDisplayName("§2" + key);
        //         List<String> lore = new ArrayList<String>();
        //         lore.add("§7Left click to select");
        //         lore.add("§7Right click to view items");
        //         lore.add("§7Extra effects and perks not listed");
        //         meta.setLore(lore);
        //         meta.setDisplayName("§2" + key);
        //         item.setItemMeta(meta);
        //         inventory.addItem(item);
        //     }
        //     player.openInventory(inventory);
        // } else {
        //     switch (arg1[0]) {
        //         case "create":
        //             if (sender.getName() == "CONSOLE" || arg1[1].length() == 0
        //                     || Material.valueOf(arg1[2].toUpperCase()) == null) {
        //                 plugin.getLogger()
        //                         .info("Command not available to console" + Material.valueOf(arg1[2].toUpperCase()));
        //                 return false;
        //             }
        //             sender.sendMessage("Known subcommand sent by: " + sender.getName());
        //             if (arg1[1].length() > 0 && plugin.getConfig().contains("classes." + arg1[1], false)) {
        //                 plugin.getConfig().set("classes." + arg1[1], null);
        //             }
        //             for (ItemStack item : player.getInventory().getStorageContents()) {
        //                 if (item != null) {
        //                     plugin.getConfig().set("classes." + arg1[1] + ".items." + item.getType(), item.serialize());
        //                 }
        //             }
        //             plugin.getConfig().set("classes." + arg1[1] + ".material", arg1[2].toUpperCase());
        //             plugin.saveConfig();
        //             sender.sendMessage("§2§lSuccess:§a The class §2" + arg1[1] + "§a was created successfully.");
        //             break;
        //         case "delete":
        //             if (arg1[1].length() > 0 && plugin.getConfig().contains("classes." + arg1[1], false)) {
        //                 plugin.getConfig().set("classes." + arg1[1], null);
        //                 plugin.saveConfig();
        //                 sender.sendMessage("§2§lSuccess: §aClass successfully deleted.");
        //             } else {
        //                 sender.sendMessage("§4§lError:§c No class was provided/found.");
        //             }
        //             break;
        //         case "list":
        //             sender.sendMessage(plugin.getConfig().getConfigurationSection("classes").getKeys(false).toString());
        //         default:
        //             break;
        //     }
        // }
        return false;
    }
}