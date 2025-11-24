package com.wolfco.main.commands;

import org.bukkit.command.CommandSender;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.argumenthandlers.StringArg;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.main.Core;

public class MiniMessage implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("minimessage");
        command.setAccessType(AccessType.PLAYER);
        command.addArguments(new StringArg(true));

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public MiniMessage(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        Core.get().getAdventure().sender(sender).sendMessage(net.kyori.adventure.text.minimessage.MiniMessage.miniMessage().deserialize((String) argumentValues[0]));
        return true;
    }
}
