package com.wolfco;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.wolfco.commands.ProjectCommand;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class Main extends JavaPlugin {
    MongoConnector mongoConnector = new MongoConnector("mongodb://154.29.72.91:27017/", "projectDB");
    BukkitAudiences adventure;
    boolean connected;

    @Override
    public void onEnable() {
        connected = mongoConnector.connect();

        if (!connected) {
            getLogger().severe("Failed to connect to the database");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        adventure = BukkitAudiences.create(this);

        getCommand("project").setExecutor(new ProjectCommand(this));

        getLogger().info("Project Manager started successfully.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Project Manager disabled.");
    }

    public MongoConnector getMongoConnector() {
        return mongoConnector;
    }

    public BukkitAudiences getAdventure() {
        return adventure;
    }

    public void sendMessage(CommandSender sender, String text) {
        adventure.sender(sender).sendMessage(MiniMessage.miniMessage().deserialize(text));
    }

}