package com.wolfco;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class Main extends JavaPlugin {
    MongoConnector mongoConnector = new MongoConnector("mongodb://localhost:27017/", "projectDB");
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

        getCommand("project").setExecutor(new com.wolfco.commands.ProjectCommand(this));
    }

    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
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