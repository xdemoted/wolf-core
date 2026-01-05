package com.wolfco.main.events;

import org.bukkit.GameRule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import com.wolfco.common.Utilities;
import com.wolfco.common.listeners.CoreListener;
import com.wolfco.main.Core;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.luckperms.api.model.user.User;

@Named("playerActivityMessageListener")
@Singleton
public class PlayerActivityMessageListener implements CoreListener {
    private final Core core;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    @Inject
    public PlayerActivityMessageListener(Core core) {
        this.core = core;
        core.getServer().getWorlds().forEach(world -> {
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, true);
            world.setGameRule(GameRule.SHOW_DEATH_MESSAGES, true);
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true) // Should run last
    public void onPlayerDeath(PlayerDeathEvent event) {
        Component deathMessage = event.deathMessage();

        if (deathMessage == null) return;

        String message = PlainTextComponentSerializer.plainText().serialize(deathMessage);

        message = message.replaceAll("\\u00A7[0-9A-FK-ORa-fk-or]", ""); // Strip color codes
        
        event.deathMessage(miniMessage.deserialize(message)); // Allow Nexo to modify death messages
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerAdvancement(PlayerAdvancementDoneEvent event) {
        String key = event.getAdvancement().getKey().asString();

        key = key.replace("minecraft:", "").replace("/", ".");
        User user = core.getLuckPerms().getUserManager().getUser(event.getPlayer().getUniqueId());

        if (user == null) {
            core.log("LuckPerms user not found: " + event.getPlayer().getName());
            return;
        }

        event.message(miniMessage.deserialize(miniMessage.serialize(Utilities.getPrefix(user)) + event.getPlayer().getName() + "<white> has made the advancement <green><lang:chat.square_brackets:\"<hover:show_text:'<green><lang:advancements.{}.title><br><lang:advancements.{}.description>'><lang:advancements.{}.title>\">".replaceAll("\\{\\}", key)));
    }
}
