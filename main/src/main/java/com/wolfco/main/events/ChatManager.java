package com.wolfco.main.events;

import java.util.concurrent.CompletableFuture;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import com.wolfco.common.Utilities;
import com.wolfco.common.listeners.CoreListener;
import com.wolfco.main.Core;
import com.wolfco.main.classes.redis.AsyncGlobalMessageEvent;
import com.wolfco.main.classes.redis.ChatMessage;
import com.wolfco.main.utility.FontUtil;

import io.papermc.paper.event.player.AsyncChatEvent;
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
    private final MiniMessage chatSerializer;

    @Inject
    public ChatManager(Core core) {
        this.core = core;
        chatSerializer = MiniMessage.builder()
                .tags(TagResolver.builder()
                        .resolver(StandardTags.color())
                        .resolver(StandardTags.decorations())
                        .build())
                .build();
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        // Variables
        String message = PlainTextComponentSerializer.plainText().serialize(event.message());
        Player player = event.getPlayer();

        ChatMessage chatMessage = new ChatMessage(core.getServerName(), player, message);

        sendChatMessage(chatMessage);
        core.getRedisManager().sendChatMessageAsync(chatMessage);
        
        event.setCancelled(true);
    }

    @EventHandler
    public void onGlobalChatMessage(AsyncGlobalMessageEvent event) {
        sendChatMessage(event.getChatMessage());
    }

    void sendChatMessage(ChatMessage chatMessage) {
        if (core.getServer().getOnlinePlayers().isEmpty()) {
            return;
        }

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

            core.getAdventure().players().sendMessage(nameText.append(messageText));
        });
    }
}