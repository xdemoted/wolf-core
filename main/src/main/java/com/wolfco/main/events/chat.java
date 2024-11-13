package com.wolfco.main.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.luckperms.api.cacheddata.CachedDataManager;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;

import com.wolfco.common.utils;
import com.wolfco.main.core;

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
        String[] metaData = utils.getMetaData(cacheData.getPermissionData(), player);
        String chatPrefix = metaData[0];
        String chatSuffix = metaData[1];

        // Color & Null Check
        if (message != null) {
            if (message.contains("¶")) {
                player.sendMessage(core.getMessage("chat.reserved"));
            } else {
                Component componentMessage = MiniMessage.miniMessage()
                        .deserialize(utils.nullCheck(lpmetaData.getPrefix())
                                + player.getName() + utils.nullCheck(lpmetaData.getSuffix()) + "<#555555>»<#aaaaaa> "
                                + chatPrefix + "<chat>" + chatSuffix,
                                (color == true ? Placeholder.parsed("chat", message)
                                        : Placeholder.unparsed("chat", message)));

                // Send to Velocity
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("globalchat");
                out.writeUTF(MiniMessage.miniMessage().serialize(componentMessage));
                player.sendPluginMessage(core, "core:main", out.toByteArray());
            }
        } else {
            player.sendMessage("§4§lError: §cMessage is null");
            core.getLogger().warning("[Wolf-Core] Message is null");
        }
        event.setCancelled(true);
    }
}
