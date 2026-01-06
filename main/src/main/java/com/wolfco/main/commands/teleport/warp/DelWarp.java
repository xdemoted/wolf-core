package com.wolfco.main.commands.teleport.warp;

import java.util.List;

import org.bukkit.command.CommandSender;
import com.wolfco.common.MessageUtility;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.main.commands.arguments.WarpArgument;
import com.wolfco.main.warps.Warp;
import com.wolfco.main.warps.WarpManager;

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
    WarpManager warpManager;

    @Override
    public boolean onCommand(CommandSourceStack commandStack, String[] args, Object[] argumentValues) {
        CommandSender sender = commandStack.getSender();
        Warp warp = (Warp) argumentValues[0];
        boolean result = warpManager.removeWarp(warp.name);
        
        if (result) {
            MessageUtility.sendPreset(sender, "warp.deleted", List.of(args[0]));
        } else {
            MessageUtility.sendPreset(sender, "warp.notfound", List.of(args[0]));
        }

        return result;
    }

}
