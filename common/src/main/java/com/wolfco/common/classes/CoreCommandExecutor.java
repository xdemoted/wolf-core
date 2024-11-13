package com.wolfco.common.classes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.utils;

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
                add("<#ffaa00>Usage: <#ff5555>/" + alias + " ");
            }
        };
        command.options.forEach(option -> {
            if (option.isRequired()) {
                result.set(0, result.get(0) + "<" + option.getType().toString() + "> ");
            } else {
                result.set(0, result.get(0) + "[" + option.getType().toString() + "] ");
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
            if (!option.isRequired()) {
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
            return "<#ffaa00>Error: <#ff5555>Too many args";
        }
        return null;
    }
    
    @Override
    default boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        String result = checkArgs(args, label);
        if (result != null) {
            utils.sendColorText(fetchCore().getAdventure().sender(sender), result);
            return false;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission(getCommand().node)) {
                utils.sendColorText(fetchCore().getAdventure().sender(sender), fetchCore().getMessage("generic.nopermission"));
                return false;
            }
        }
        return execute(sender, command, label, args);
    }
    boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args);

    @Override
    default List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command bukkitCommand, String alias, String[] args) {
        TabCompleter tabComplete = new TabCompleter(fetchCore());
        return tabComplete.runTabComplete(getCommand(), sender, bukkitCommand, alias, args);
    }

    default ArgumentInterface getArgument(int i) {
        return getCommand().getArgument(i);
    }
}
