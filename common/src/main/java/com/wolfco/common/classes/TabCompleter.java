package com.wolfco.common.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.wolfco.common.commands.arguments.SubCommandArg;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class TabCompleter {
    @Inject
    public CorePlugin core;

    public List<String> runTabComplete(Command command, CommandSourceStack commandStack, String[] args) {
        List<String> result;
        result = new ArrayList<>();

        if (args.length > command.options.size()) {
            if (command.options.getLast() instanceof SubCommandArg subcommand) {
                Command command2 = subcommand.get(args[command.options.size() - 1].toLowerCase());

                if (command2 != null) {
                    return runTabComplete(command2, commandStack, Arrays.copyOfRange(args, command.options.size(), args.length));
                } else {
                    return result;
                }
            } else {
                return result;
            }
        }

        for (String arg : args) {
            core.getLogger().info("Arg: " + arg);
        }

        ArgumentInterface argument = command.getArgument(args.length - 1);
        String lastArg = args[args.length - 1].toLowerCase();

        argument.getOptions(commandStack, args).forEach(option -> {
            if (option.toLowerCase().startsWith(lastArg)) {
                result.add(option);
            }
        });

        return result;
    }
}
