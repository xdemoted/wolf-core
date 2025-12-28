package com.wolfco.common.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.wolfco.common.commands.arguments.SubCommandArg;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class TabCompleter {
    @Inject
    private CorePlugin core;

    public List<String> runTabComplete(Command command, CommandSender sender, org.bukkit.command.Command bukkitCommand,
            String alias, String[] args) {
        List<String> result;
        result = new ArrayList<>();

        if (args.length > command.options.size()) {
            if (command.options.getLast() instanceof SubCommandArg subcommand) {
                Command command2 = subcommand.get(args[command.options.size() - 1].toLowerCase());

                if (command2 != null) {
                    return runTabComplete(command2, sender, bukkitCommand, alias, Arrays.copyOfRange(args, command.options.size(), args.length));
                } else {
                    return result;
                }
            } else {
                return result;
            }
        }

        ArgumentInterface argument = command.getArgument(args.length - 1);
        String lastArg = args[args.length - 1].toLowerCase();

        argument.getOptions(core, sender, bukkitCommand, args).forEach(option -> {
            if (option.toLowerCase().startsWith(lastArg)) {
                result.add(option);
            }
        });

        return result;
    }
}
