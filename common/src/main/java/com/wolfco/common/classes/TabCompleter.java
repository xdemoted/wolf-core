package com.wolfco.common.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

public class TabCompleter {
    private CorePlugin core;

    public TabCompleter(CorePlugin core) {
        this.core = core;
    }

    public List<String> tabComplete(Command command, CommandSender sender, org.bukkit.command.Command bukkitCommand,
            String alias, String[] args) {
        List<String> result = new ArrayList<String>();

        if (args.length > command.options.size()) {
            if (command.options.getLast().getType() == ArgumentType.SUBCOMMAND) {
                subcommand subcommand = (subcommand) command.options.getLast();
                Command command2 = subcommand.get(args[command.options.size() - 1].toLowerCase());

                if (command2 != null) {
                    return tabComplete(command2, sender, bukkitCommand, alias, Arrays.copyOfRange(args, command.options.size(), args.length));
                } else {
                    return result;
                }
            } else {
                return result;
            }
        }

        ArgumentInterface argument = command.getArgument(args.length - 1);
        String lastArg = args[args.length - 1].toLowerCase();

        argument.options(core, sender, bukkitCommand, args).forEach(option -> {
            if (option.toLowerCase().startsWith(lastArg)) {
                result.add(option);
            }
        });

        return result;
    }
}
