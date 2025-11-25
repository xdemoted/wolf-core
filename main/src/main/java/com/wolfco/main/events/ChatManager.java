package com.wolfco.main.events;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.wolfco.common.Utilities;
import com.wolfco.main.Core;
import com.wolfco.main.classes.redis.ChatMessage;
import com.wolfco.main.classes.redis.GlobalMessageEvent;
import com.wolfco.main.utility.FontUtil;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.luckperms.api.cacheddata.CachedDataManager;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;

public class ChatManager implements Listener, PluginMessageListener {

    Core core;
    MiniMessage serializer;

    public ChatManager(Core core) {
        this.core = core;
        serializer = MiniMessage.builder()
                .tags(TagResolver.builder()
                        .resolver(StandardTags.color())
                        .resolver(StandardTags.decorations())
                        .build())
                .build();
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
            chatSuffix = suffix.split(";")[1];
            suffix = suffix.split(";")[0];
        }

        String formatting = FontUtil.parseNameTag(prefix + player.getName())
                + suffix + " <#555555>» "
                + chatPrefix + "<message>" + chatSuffix;

        core.getRedisManager().sendChatMessage(formatting, message, color);

        core.getAdventure().players().sendMessage(formatChatMessage(formatting, message, color));

        event.setCancelled(true);
    }

    @EventHandler
    public void onGlobalMessage(GlobalMessageEvent event) {
        ChatMessage chatMessage = event.getChatMessage();
        Component formattedMessage = formatChatMessage(chatMessage.getFormatData(), chatMessage.getMessageData(),
                chatMessage.getColorEnabled());

        core.getAdventure().players().sendMessage(formattedMessage);
    }

    public Component formatChatMessage(String formatData, String messageData, boolean colorEnabled) {
        String[] formattingParts = formatData.split("<message>", 2);

        if (formattingParts.length != 2) {
            core.getLogger().log(Level.WARNING, "Invalid formatting string received: {0}", formatData);
            return Component.text(messageData);
        }

        Component preMessage = MiniMessage.miniMessage().deserialize(formattingParts[0] + "<reset>");
        Component postMessage = MiniMessage.miniMessage().deserialize(formattingParts[1]);
        Component fullMessage = preMessage
                .append(colorEnabled ? serializer.deserialize(messageData) : Component.text(messageData))
                .append(postMessage);

        return fullMessage;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!"core:main".equals(channel))
            return;

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();

        if (!subchannel.equals("globalchat"))
            return;

        String senderName = in.readUTF();
        String formatting = in.readUTF(); // formatting string (contains <message>)
        String msg = in.readUTF();
        boolean color = in.readBoolean();
        // Example: replace placeholder and broadcast (adjust color handling as needed)
        String out = formatting.replace("<message>", msg).replace(senderName, senderName);
        Core.get().getServer().broadcastMessage(out);
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
