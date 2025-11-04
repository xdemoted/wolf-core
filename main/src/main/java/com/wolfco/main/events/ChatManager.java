package com.wolfco.main.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.wolfco.common.Utilities;
import com.wolfco.main.Core;
import com.wolfco.main.utility.FontUtil;

import net.luckperms.api.cacheddata.CachedDataManager;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;

public class ChatManager implements Listener {

    Core core;

    public ChatManager(Core core) {
        this.core = core;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        // Variables
        String message = event.getMessage();
        Player player = event.getPlayer();
        User user = core.getLuckPerms().getUserManager().getUser(player.getUniqueId());
        CachedDataManager cacheData = user.getCachedData();
        CachedMetaData lpmetaData = cacheData.getMetaData();
        Boolean color = cacheData.getPermissionData().checkPermission("wolf-co.chat.color").asBoolean();
        String prefix = Utilities.nullCheck(lpmetaData.getPrefix());
        String suffix = Utilities.nullCheck(lpmetaData.getSuffix());
        String chatPrefix = "";
        String chatSuffix = "";

        if (prefix.contains(";")) {
            chatPrefix = prefix.split(";")[1];
            prefix = prefix.split(";")[0];
        }
        if (suffix.contains(";")) {
            chatPrefix = suffix.split(";")[1];
            suffix = suffix.split(";")[0];
        }

        String formatting = FontUtil.parseNameTag(prefix + player.getName())
                + suffix + " <#555555>» "
                + chatPrefix + "<message>" + chatSuffix;

        // Send to Velocity
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("globalchat");
        out.writeUTF(player.getName());
        out.writeUTF(formatting);
        out.writeUTF(message);
        out.writeBoolean(color);
        player.sendPluginMessage(core, "core:main", out.toByteArray());
        event.setCancelled(true);
    }

    public void sendGlobalBroadcast(Player player, String message) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("broadcast");
        out.writeUTF(message);
        out.writeBoolean(false);
        player.sendPluginMessage(core, "core:main", out.toByteArray());
    }

    public void changeAFK(Player player, boolean afk) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("afk");
        out.writeUTF(player.getName());
        out.writeBoolean(afk);
        player.sendPluginMessage(core, "core:main", out.toByteArray());
    }
}
