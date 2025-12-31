package com.wolfco.common.commands;

import java.util.List;

import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.classes.CorePlugin;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class CommandLoader {
    @Inject
    public CorePlugin core;
    
    private List<CoreCommand> commands;

    public void registerAll() {
        this.commands = core.getScope().list(CoreCommand.class);

        core.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commandsRegistrar -> {
            for (CoreCommand command : commands) {
                commandsRegistrar.registrar().register(command.getCommand().getName(), command.getCommand().getAliases(), command);
            }
        });
    }
}
