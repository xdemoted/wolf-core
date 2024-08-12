package risingdeathx2.spigot.wolfcore;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import dev.dejvokep.boostedyaml.YamlDocument;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedDataManager;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;

public class core extends JavaPlugin implements Listener {
    public LuckPerms lp;
    private BukkitAudiences adventure;
    public YamlDocument config;
    static public String prefix = "♆ ";

    public @NonNull BukkitAudiences adventure() {
        if (this.adventure == null) {
            this.adventure = BukkitAudiences.create(this);
        }
        return this.adventure;
    }

    @Override
    public void onEnable() {
        try {
            lp = LuckPermsProvider.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        adventure();
        config = getConfig("config.yml");
        this.getLogger().info("[Wolf-Core] Plugin enabled");
        new command(this);
        getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "core:main");
    }

    @Override
    public void onDisable() {
        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(this, "core:main");
        this.getLogger().info("[Wolf-Core] Plugin disabled");
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        // Variables
        String message = event.getMessage();
        Player player = event.getPlayer();
        User user = this.lp.getUserManager().getUser(player.getUniqueId());
        CachedDataManager cacheData = user.getCachedData();
        CachedMetaData lpmetaData = cacheData.getMetaData();
        Boolean color = cacheData.getPermissionData().checkPermission("wolf-co.chat.color").asBoolean();
        String[] metaData = utils.getMetaData(cacheData.getPermissionData(),player);
        String chatPrefix = utils.colorizeText(metaData[0]);
        String chatSuffix = utils.colorizeText(metaData[1]);
        // Color & Null Check
        if (message != null) {
            if (message.contains("¶")) {
                player.sendMessage("§4§lError: §cThe \"¶\" character is reserved for internal use.");
            } else {
                message = utils.colorizeText(
                        utils.nullCheck(lpmetaData.getPrefix()) + player.getName() + utils.nullCheck(lpmetaData.getSuffix()) + " <#555555>»<#aaaaaa> " + utils.nullCheck(chatPrefix))
                        + (color == true ? utils.colorizeText(message) : message) + chatSuffix;
                // Send to Velocity
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("globalchat");
                out.writeUTF(message);
                player.sendPluginMessage(this, "core:main", out.toByteArray());
            }
        } else {
            player.sendMessage("§4§lError: §cMessage is null");
            getLogger().warning("[Wolf-Core] Message is null");
        }
        event.setCancelled(true);
    }
    public YamlDocument getConfig(String fileName) {
        Path configFile = getDataFolder().toPath().resolve(fileName);
        YamlDocument config;
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName)) {
            config = YamlDocument.create(configFile.toFile(), is);
        } catch (IOException e) {
            config = null;
        }
        return config;
    }
}