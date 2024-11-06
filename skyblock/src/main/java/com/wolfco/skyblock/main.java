package com.wolfco.skyblock;

import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import com.wolfco.skyblock.handlers.commandLoader;
import com.wolfco.skyblock.handlers.damageHandler;
import com.wolfco.skyblock.handlers.entityHandler;

public class main extends JavaPlugin {
    public entityHandler entityHandler;
    public commandLoader commandLoader;
    // This code is called after the server starts and after the /reload command
    @Override
    public void onEnable() {
        new events(this);
        commandLoader = new commandLoader(this);
        entityHandler = new entityHandler(this);
        getServer().getPluginManager().registerEvents(new damageHandler(), this);
    }

    // This code is called before the server stops and after the /reload command
    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "{0}.onDisable()", this.getClass().getName());
    }
}
