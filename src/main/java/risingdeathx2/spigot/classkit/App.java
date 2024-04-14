package risingdeathx2.spigot.classkit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.joml.Vector2d;
import org.joml.Vector3d;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.md_5.bungee.api.ChatColor;

public class App extends JavaPlugin implements PluginMessageListener, Listener {
    public LuckPerms lp;
    private Vector2d pos1 = new Vector2d(-153,-2);
    private Vector2d pos2 = new Vector2d(154,-310);
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
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "core:main", this);
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
        this.getLogger().info("Sending Message");
        this.getLogger().info(displayName + " §8»§f " + message);
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Forward");
        out.writeUTF("ALL");
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
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        this.getLogger().info("Received message from " + channel);
        if (!channel.equals("core:main")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals("chat")) {
            short len = in.readShort();
            byte[] msgbytes = new byte[len];
            in.readFully(msgbytes);

            DataInputStream msgIn = new DataInputStream(new ByteArrayInputStream(msgbytes));
            String msg;
            try {
                msg = msgIn.readUTF();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            if (msg != null) Bukkit.broadcastMessage(msg);
        }
    }
    @EventHandler
    public void PhysicsUpdate(BlockPhysicsEvent event) {
        Vector3d pos = new Vector3d(event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ());
        if (pos.x > pos1.x && pos.x < pos2.x && pos.z < pos1.y && pos.z > pos2.y)
        event.setCancelled(true);
    }
    public void log(String str) {
        getLogger().info(str);
    }
}