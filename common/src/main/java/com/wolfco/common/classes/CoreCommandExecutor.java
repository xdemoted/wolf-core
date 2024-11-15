package com.wolfco.common.classes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.Utilities;

import net.kyori.adventure.audience.Audience;

public interface CoreCommandExecutor extends CommandExecutor, org.bukkit.command.TabCompleter {

    Command getCommand();

    abstract CorePlugin fetchCore();

    default String getUsage() {
        return getUsage(getCommand().name);
    }
    
    default String getUsage(String alias) {
        Command command = getCommand();
        List<String> result = new ArrayList<>() {
            {
                add("<#aa00><b>Usage:</b> <#ff5555>/" + alias + " ");
            }
        };
        command.options.forEach(option -> {
            if (option.isRequired()) {
                result.set(0, result.get(0) + "<" + option.getName() + "> ");
            } else {
                result.set(0, result.get(0) + "[" + option.getName() + "] ");
            }
        });
        return result.get(0);
    }

    default Integer getRequiredArgs() {
        Command command = getCommand();
        List<Integer> result = new ArrayList<>() {
            {
                add(0);
            }
        };
        command.options.forEach(option -> {
            if (option.isRequired()) {
                result.set(0, result.get(0) + 1);
            }
        });
        return result.get(0);
    }
    
    default String checkArgs(String[] args, String alias) {
        Command command = getCommand();
        Integer requiredArgs = getRequiredArgs();
        if (args.length < requiredArgs) {
            return getUsage(alias);
        } else if (args.length > command.options.size()) {
            return "<#aa0000><b>Error:</b> <#ff5555>Too many args";
        }
        return null;
    }
    
    @Override
    default boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        Audience audience = fetchCore().getAdventure().sender(sender);
        String result = checkArgs(args, label);
        
        Utilities.sendColorText(audience, getRequiredArgs().toString());

        if (result != null) {
            Utilities.sendColorText(audience, result);
            return false;
        }
        if (sender instanceof Player player) {
            if (!player.hasPermission(getCommand().node)) {
                Utilities.sendColorText(audience, fetchCore().getMessage("generic.nopermission"));
                return false;
            }
        }

        Object[] argumentValues;
        try {
            argumentValues = getCommand().getValues(fetchCore(), sender, command, args);
        } catch (IllegalArgumentException e) {
            Utilities.sendColorText(audience, fetchCore().getMessage("error.base", List.of(e.getMessage())));
            return false;
        }
        return execute(sender, command, label, args, argumentValues);
    }
    
    boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] ArgumentValues);

    @Override
    default List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command bukkitCommand, String alias, String[] args) {
        TabCompleter tabComplete = new TabCompleter(fetchCore());
        return tabComplete.runTabComplete(getCommand(), sender, bukkitCommand, alias, args);
    }

    default ArgumentInterface getArgument(int i) {
        return getCommand().getArgument(i);
    }
}
