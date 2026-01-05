package com.wolfco.main.commands;

import org.bukkit.command.CommandSender;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.common.commands.arguments.StringArg;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import jakarta.inject.Singleton;

@Singleton
public class MiniMessage implements CoreCommand {
    @Override
    public Command getCommand() {
        Command command = new Command().setName("minimessage");
        command.setAccessType(AccessType.PLAYER);
        command.addArguments(new StringArg(true));

        return command;
    }
    
    @Override
    public boolean onCommand(CommandSourceStack commandStack, String[] args, Object[] argumentValues) {
        CommandSender sender = commandStack.getSender();
        sender.sendMessage(net.kyori.adventure.text.minimessage.MiniMessage.miniMessage().deserialize((String) argumentValues[0]));
        return true;
    }
}
