package com.wolfco.main.commands;

import java.io.IOException;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.main.Core;
import com.wolfco.main.classes.customargs.WarpArgument;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class DelWarp implements CoreCommand {
    @Override
    public Command getCommand() {
        Command command = new Command().setName("delwarp");
        command.addArguments(new WarpArgument(true));

        return command;
    }

    @Inject
    Core core;

    @Override
    public boolean onCommand(CommandSourceStack commandStack, String[] args, Object[] argumentValues) {
        CommandSender sender = commandStack.getSender();
        boolean result = core.getWarps().remove(args[0]);
        if (result) {
            core.sendPreset(sender, "warp.deleted", List.of(args[0]));
        } else {
            core.sendPreset(sender, "warp.notfound", List.of(args[0]));
        }

        try {
            core.getWarps().save();
        } catch (IOException e) {
            sender.sendMessage("An error occurred while saving warps");
        }

        return result;
    }

}
