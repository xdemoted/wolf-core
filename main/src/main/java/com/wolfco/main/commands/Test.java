package com.wolfco.main.commands;

import com.wolfco.common.classes.Argument;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.Subcommand;
import com.wolfco.common.classes.types.ArgumentType;
import com.wolfco.main.Core;
import com.wolfco.main.classes.customargs.WarpArgument;

public class Test implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command1 = new Command("warps");
        command1.setNode("wolfcore.warps");
        command1.addArgument(new WarpArgument(false));
        Command command2 = new Command("info");
        command2.setNode("wolfcore.warpinfo");
        command2.addArgument(new Argument(ArgumentType.PLAYER, true));

        Command command = new Command("test");
        command.setDescription("Get information about a warp");
        command.setNode("wolfcore.test");
        command.addArgument(new Subcommand(true).add(command1).add(command2));

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public Test(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        return false; // TODO Add test command logic
    }
}
