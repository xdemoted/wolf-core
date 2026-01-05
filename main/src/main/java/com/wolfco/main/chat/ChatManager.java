package com.wolfco.main.chat;

import java.util.concurrent.CompletableFuture;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import com.wolfco.common.Utilities;
import com.wolfco.common.listeners.CoreListener;
import com.wolfco.main.Core;
import com.wolfco.main.redis.RedisManager;
import com.wolfco.main.redis.classes.AsyncGlobalMessageEvent;
import com.wolfco.main.redis.classes.ChatMessage;
import com.wolfco.main.utility.FontUtil;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.luckperms.api.cacheddata.CachedDataManager;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;

@Named("chatManager")
@Singleton
public class ChatManager implements CoreListener {
    private final Core core;
    private final RedisManager redisManager;
    private final MiniMessage chatSerializer;

    @Inject
    public ChatManager(Core core, RedisManager redisManager) {
        this.core = core;
        this.redisManager = redisManager;
        chatSerializer = MiniMessage.builder()
                .tags(TagResolver.builder()
                        .resolver(StandardTags.color())
                        .resolver(StandardTags.decorations())
                        .build())
                .build();
    }

    public void handleChat(Player player, Component message) {
        String strMessage = PlainTextComponentSerializer.plainText().serialize(message);

        ChatMessage chatMessage = new ChatMessage(core.getServerName(), player, strMessage);

        sendChatMessage(chatMessage);
        redisManager.sendChatMessageAsync(chatMessage);
    }

    @EventHandler
    public void onGlobalChatMessage(AsyncGlobalMessageEvent event) {
        if (core.getServer().getOnlinePlayers().isEmpty()) {
            return;
        }

        sendChatMessage(event.getChatMessage());
    }

    private void sendChatMessage(ChatMessage chatMessage) {
        CompletableFuture<User> user = core.getLuckPerms().getUserManager().loadUser(chatMessage.getUUID());

        user.thenAcceptAsync(u -> {
            CachedDataManager cacheData = u.getCachedData();
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

            String nameTag = prefix + chatMessage.getName() + suffix;

            nameTag = FontUtil.parseNameTag(nameTag);

            String message = chatMessage.getMessage();
            core.log(message);

            Component nameText = MiniMessage.miniMessage().deserialize(nameTag + " <#555555>» ");
            Component messageText = color
                    ? chatSerializer.deserialize(chatPrefix + message + chatSuffix)
                    : Component.text(message);

            core.getServer().sendMessage(nameText.append(messageText));
        });
    }
}