package com.wolfco.common.classes;

import java.util.List;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import com.wolfco.common.commands.CommandService;
import com.wolfco.common.commands.CommandValues;

public interface CoreCommand extends CommandExecutor, org.bukkit.command.TabCompleter {
    Command getCommand();

    default String getUsage() {
        return getUsage(getCommand().name);
    }

    default String getUsage(String alias) {
        return CommandService.getUsage(getCommand(), alias);
    }

    default Integer getRequiredArgs() {
        return CommandService.getRequiredArgs(getCommand());
    }

    default String checkArgs(String[] args, String alias) {
        return CommandService.checkArgs(getCommand(), args, alias);
    }

    default String[] parseQuotation(String[] args) {
        return CommandService.parseQuotation(args);
    }

    @Override
    default boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label,
            String[] args) {
        CommandValues commandValues = CommandService.preExecuteCommand(getCommand(), sender, command, label, args);

        if (commandValues == null) {
            return false;
        }

        return execute(sender, command, label, args, commandValues.argumentValues);
    }

    abstract boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias,
            String[] args, Object[] argumentValues);

    @Override
    default List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command bukkitCommand, String alias,
            String[] args) {
        TabCompleter tabComplete = new TabCompleter();
        return tabComplete.runTabComplete(getCommand(), sender, bukkitCommand, alias, args);
    }

    default ArgumentInterface getArgument(int i) {
        return getCommand().getArgument(i);
    }
}
