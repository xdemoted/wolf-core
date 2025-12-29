package com.wolfco.common.classes;

import java.util.List;

import com.wolfco.common.commands.CommandService;
import com.wolfco.common.commands.CommandValues;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;

public interface CoreCommand extends BasicCommand {
    Command getCommand();

    default String getUsage() {
        return CommandService.getUsage(getCommand());
    }

    default Integer getRequiredArgs() {
        return CommandService.getRequiredArgs(getCommand());
    }

    default String checkArgs(String[] args) {
        return CommandService.checkArgs(getCommand(), args);
    }

    default String[] parseQuotation(String[] args) {
        return CommandService.parseQuotation(args);
    }

    @Override
    default void execute(CommandSourceStack commandStack, String[] args) {
        CommandValues commandValues = CommandService.preExecuteCommand(getCommand(), commandStack, args);

        if (commandValues == null) {
            return;
        }

        onCommand(commandStack, args, commandValues.argumentValues);
    }

    abstract boolean onCommand(CommandSourceStack commandStack,
            String[] args, Object[] argumentValues);

    @Override
    default List<String> suggest(CommandSourceStack commandStack,
            String[] args) {
        TabCompleter tabComplete = new TabCompleter();
        return tabComplete.runTabComplete(getCommand(), commandStack, args);
    }

    default ArgumentInterface getArgument(int i) {
        return getCommand().getArgument(i);
    }
}
