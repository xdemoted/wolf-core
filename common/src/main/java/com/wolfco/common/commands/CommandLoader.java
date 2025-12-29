package com.wolfco.common.commands;

import java.util.List;

import org.bukkit.plugin.Plugin;

import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.classes.CorePlugin;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class CommandLoader {
    @Inject
    public CorePlugin core;

    @Inject
    List<CoreCommand> commands;

    public void registerAll() {
        core.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commandsRegistrar -> {
            for (CoreCommand command : commands) {
                commandsRegistrar.registrar().register(command.getCommand().getName(), command);
            }
        });
    }
}
