package com.wolfco.common.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CorePlugin;
import com.wolfco.common.classes.types.AccessType;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class CommandService {
    @Inject
    static CorePlugin core;

    public static String getUsage(Command command) {
        List<String> result = new ArrayList<>();
        result.add("<#aa00><b>Usage:</b> <#ff5555>/" + command.getName() + " ");

        command.getArguments().forEach(option -> {
            if (option.isRequired()) {
                result.set(0, result.get(0) + "<" + option.getName() + "> ");
            } else {
                result.set(0, result.get(0) + "[" + option.getName() + "] ");
            }
        });
        return result.get(0);
    }

    public static Integer getRequiredArgs(Command command) {
        final int[] result = { 0 };
        command.getArguments().forEach(option -> {
            if (option.isRequired()) {
                result[0]++;
            }
        });
        return result[0];
    }

    public static String checkArgs(Command command, String[] args) {
        Integer requiredArgs = getRequiredArgs(command);
        if (args.length < requiredArgs) {
            return getUsage(command);
        } else if (args.length > command.getArguments().size()) {
            return "<#aa0000><b>Error:</b> <#ff5555>Too many args";
        }
        return null;
    }

    public static String[] parseQuotation(String[] args) {
        String joinedArgs = String.join(" ", args);
        List<String> parsedArgs = new ArrayList<>();
        Matcher matcher = Pattern.compile("\"([^\"]*)\"|(\\S+)").matcher(joinedArgs);
        while (matcher.find()) {
            if (matcher.group(1) != null) {
                parsedArgs.add(matcher.group(1));
            } else {
                parsedArgs.add(matcher.group(2));
            }
        }

        return parsedArgs.toArray(String[]::new);
    }

    public static CommandValues preExecuteCommand(Command command, CommandSourceStack commandStack, String[] args) {
        String result = checkArgs(command, args);
        AccessType accessType = command.getAccessType();
        CommandSender sender = commandStack.getSender();

        if (sender instanceof Player player && !player.hasPermission(command.getNode())) { // Permission Check
            core.sendPreset(sender, "generic.nopermission");
            return null;
        }

        if (accessType == AccessType.PLAYER && !(sender instanceof Player)) { // Access Check
            core.sendPreset(sender, "generic.noconsole");
            return null;
        } else if (accessType == AccessType.CONSOLE && sender instanceof Player) {
            core.sendPreset(sender, "generic.noplayer");
            return null;
        }

        if (result != null) { // Argument Check
            core.sendMessage(sender, result);
            return null;
        }

        Object[] argumentValues;

        try {
            argumentValues = command.getValues(commandStack, args);
        } catch (IllegalArgumentException e) {
            core.log(e.toString());
            core.sendPreset(sender, "error.base", List.of(e.getMessage()));
            return null;
        }

        return new CommandValues(commandStack, args, argumentValues);
    }
}
