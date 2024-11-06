package com.wolfco.velocity.types;

import javax.annotation.Nonnull;

import com.velocitypowered.api.proxy.Player;

import dev.dejvokep.boostedyaml.YamlDocument;

public class PlayerData extends OfflinePlayer {
    public boolean afk = false;
    public Player host;

    public PlayerData(Player host,@Nonnull YamlDocument data) {
        super(data);
        this.host = host;
        this.data = data;
        setIP(host.getRemoteAddress().getAddress().getHostAddress());
        setUsername(host.getUsername());
        save();
    }

}