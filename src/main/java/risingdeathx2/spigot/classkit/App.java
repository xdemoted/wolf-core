package risingdeathx2.spigot.classkit;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.md_5.bungee.api.ChatColor;

public class App extends JavaPlugin implements Listener {
    public LuckPerms lp;
    @Override
    public void onEnable() {
        try {
            lp = LuckPermsProvider.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.getLogger().info("[Wolf-Core] Plugin enabled");
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginCommand("class").setExecutor(new Command(this));
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "core:main");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("[Wolf-Core] Plugin disabled");
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(this, "core:main");
    }
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        Player player = event.getPlayer();
        User user = this.lp.getUserManager().getUser(player.getUniqueId());
        String prefix = user.getCachedData().getMetaData().getPrefix();
        String suffix = user.getCachedData().getMetaData().getSuffix();
        if (user.getCachedData().getPermissionData().checkPermission("wolf-co.chat.color").asBoolean()) {
            message = ChatColor.translateAlternateColorCodes('&', message);
        }
        String displayName = (prefix!=null ? prefix : "") + player.getName() + (suffix!=null ? suffix : "");
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', displayName) + " §8»§f " + message);

        // Send message across network
        this.getLogger().info(displayName + " §8»§f " + message);
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("chat");

        ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
        DataOutputStream msgOut = new DataOutputStream(msgbytes);
        try {
            msgOut.writeUTF(displayName + " §8»§f " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.writeShort(msgbytes.toByteArray().length);
        out.write(msgbytes.toByteArray());
        
        player.sendPluginMessage(this, "core:main", out.toByteArray());
        event.setCancelled(true);
    }
}