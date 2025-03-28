package com.wolfco.velocity.types;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;

public interface Command extends SimpleCommand {
    void onCommand(CommandSource sender, String[] args);

    @Override
    public boolean hasPermission(Invocation invocation);

    @Override
    default void execute(Invocation invocation) {
        onCommand(invocation.source(), invocation.arguments());
    }
}
