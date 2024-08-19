package risingdeathx2.spigot.wolfcore.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.luckperms.api.cacheddata.CachedDataManager;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import risingdeathx2.spigot.wolfcore.core;

public class chat implements Listener {
    public core core;

    public chat(core core) {
        this.core = core;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        // Variables
        String message = event.getMessage();
        Player player = event.getPlayer();
        User user = core.lp.getUserManager().getUser(player.getUniqueId());
        CachedDataManager cacheData = user.getCachedData();
        CachedMetaData lpmetaData = cacheData.getMetaData();
        Boolean color = cacheData.getPermissionData().checkPermission("wolf-co.chat.color").asBoolean();
        String[] metaData = risingdeathx2.spigot.wolfcore.utils.getMetaData(cacheData.getPermissionData(),player);
        String chatPrefix = risingdeathx2.spigot.wolfcore.utils.colorizeText(metaData[0]);
        String chatSuffix = risingdeathx2.spigot.wolfcore.utils.colorizeText(metaData[1]);
        // Color & Null Check
        if (message != null) {
            if (message.contains("¶")) {
                player.sendMessage(core.getMessage("chat.reserved"));
            } else {
                message = risingdeathx2.spigot.wolfcore.utils.colorizeText(
                    risingdeathx2.spigot.wolfcore.utils.nullCheck(lpmetaData.getPrefix()) + player.getName() + risingdeathx2.spigot.wolfcore.utils.nullCheck(lpmetaData.getSuffix()) + " <#555555>»<#aaaaaa> " + risingdeathx2.spigot.wolfcore.utils.nullCheck(chatPrefix))
                        + (color == true ? risingdeathx2.spigot.wolfcore.utils.colorizeText(message) : message) + chatSuffix;
                // Send to Velocity
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("globalchat");
                out.writeUTF(message);
                player.sendPluginMessage(core, "core:main", out.toByteArray());
            }
        } else {
            player.sendMessage("§4§lError: §cMessage is null");
            core.getLogger().warning("[Wolf-Core] Message is null");
        }
        event.setCancelled(true);
    }
}
