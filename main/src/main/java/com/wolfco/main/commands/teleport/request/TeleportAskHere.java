package com.wolfco.main.commands.teleport.request;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.common.commands.arguments.PlayerArg;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class TeleportAskHere implements CoreCommand {
    @Inject
    TeleportAsk teleportAsk;

        @Override
    public Command getCommand() {
        Command command = new Command().setName("teleportaskhere");
        command.setAccessType(AccessType.PLAYER);
        command.addArguments(new PlayerArg(true).includeSender(false));
        command.addAliases("tpahere");
        return command;
    }

    @Override
    public boolean onCommand(CommandSourceStack commandStack, String[] args,
            Object[] argumentValues) {
        return teleportAsk.onCommandAlias(commandStack, TeleportAsk.TPAHERE, args, argumentValues);
    }
}
