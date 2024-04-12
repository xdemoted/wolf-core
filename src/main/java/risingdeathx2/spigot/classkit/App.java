package risingdeathx2.spigot.classkit;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;

public class App extends JavaPlugin implements Listener, PluginMessageListener {
    public LuckPerms lp;
    @Override
    public void onEnable() {
        try {
            lp = LuckPermsProvider.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.getLogger().info("[Wolf-Core Messenger] Plugin enabled");
        getServer().getPluginManager().registerEvents(new EventManager(this), this);
        getServer().getPluginCommand("class").setExecutor(new Command(this));
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "bungeecord:main");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "bungeecord:main", this);
    }

    @Override
    public void onDisable() {
        this.getLogger().info("[Wolf-Core Messenger] Plugin disabled");
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(this, "bungeecord:main");
    }
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        this.getLogger().info("Received message from " + channel);
        if (!channel.equals("bungeecord:main")) {
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
    public void log(String str) {
        getLogger().info(str);
    }
}