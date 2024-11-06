package com.wolfco.velocity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import dev.dejvokep.boostedyaml.YamlDocument;
import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import java.nio.file.Path;
import java.io.IOException;
import java.io.InputStream;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.wolfco.velocity.JDA.JDAListener;
import com.wolfco.velocity.events.events;
import com.wolfco.velocity.events.playerManager;
import com.wolfco.velocity.commands.*;

@Plugin(id = "velocicore", name = "Veloci-Core", version = "1.0", description = "A core plugin for the Wolf Co. network", authors = {
        "Demoted" })
public class wolfcore {
    @Inject
    @DataDirectory
    public Path dataDirectory;
    @Inject
    public Logger logger;
    @Inject
    public ProxyServer server;
    public playerManager playerManager;
    public LuckPerms lp;
    public YamlDocument config;
    private JDAListener jda;
    public Map<UUID, String> playerChannels = new HashMap<>();

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        config = getConfig("config.yml");
        try {
            lp = LuckPermsProvider.get();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        try {
            jda = new JDAListener(this);
        } catch (InterruptedException e) {
            logger.severe(e.getMessage());
        }
        if (jda != null) {
            server.getEventManager().register(this, new events(this, jda));
        }
        loadCommands();
        playerManager = new playerManager(this);
        logger.info("Wolf-Core Loaded Successfully");
    }

    public String removeFormatting(String str) {
        return str.replaceAll("§[0-9a-ek-or]", "");
    }

    public void broadcast(Component message) {
        server.sendMessage(message);
    }

    public YamlDocument getConfig(String fileName) {
        return getConfig(fileName, dataDirectory);
    }

    public YamlDocument getConfig(String fileName, Path parent) {
        Path configFile = parent.resolve(fileName);
        YamlDocument config;
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName)) {
            config = YamlDocument.create(configFile.toFile(), is);
        } catch (IOException e) {
            try {
                config = YamlDocument.create(configFile.toFile());
            } catch (IOException e1) {
                config = null;
            }
        }
        return config;
    }

    public void loadCommands() {
        new seen(this, server.getCommandManager());
        new list(this, server.getCommandManager());
        new kick(this, server.getCommandManager());
        new ban(this, server.getCommandManager());
        new nick(this, server.getCommandManager());
    }

    public String displayname(Player player) {
        return utils.displayname(player, this);
    }
}
