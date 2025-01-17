package com.wolfco.velocity;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.wolfco.velocity.events.events;
import com.wolfco.velocity.events.playerManager;

import dev.dejvokep.boostedyaml.YamlDocument;
import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;

@Plugin(id = "velocicore", name = "Veloci-Core", version = "1.0", description = "A core plugin for the Wolf Co. network", authors = {
        "Demoted" })

public class wolfcore {
    
    public  final Path dataDirectory;
    public final ProxyServer server;
    public final Logger logger;

    public playerManager playerManager;
    public events events;
    public LuckPerms lp;
    public YamlDocument config;
    public Map<UUID, String> playerChannels = new HashMap<>();

    @Inject
    public wolfcore(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        config = getConfig("config.yml");
        
        try {
            lp = LuckPermsProvider.get();
        } catch (IllegalStateException e) {
            logger.warn(e.getMessage());
        }

        playerManager = new playerManager(this);

        server.getEventManager().register(this, new events(this));

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
        YamlDocument configDocument;
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName)) {
            configDocument = YamlDocument.create(configFile.toFile(), is);
        } catch (IOException e) {
            try {
                configDocument = YamlDocument.create(configFile.toFile());
            } catch (IOException e1) {
                configDocument = null;
            }
        }
        return configDocument;
    }
}
