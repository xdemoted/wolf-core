package risingdeathx2.spigot.wolfcore.classes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import risingdeathx2.spigot.wolfcore.core;
import risingdeathx2.spigot.wolfcore.utils;

public interface CoreCommandExecutor extends CommandExecutor {
    Command getCommand();
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
            if (option.optional) {
                result.set(0, result.get(0) + "[" + option.name + "] ");
            } else {
                result.set(0, result.get(0) + "<" + option.name + "> ");
            }
        });
        return result.get(0);
    }
    default Integer requiredArgs() {
        Command command = getCommand();
        List<Integer> result = new ArrayList<>() {
            {
                add(0);
            }
        };
        core core = risingdeathx2.spigot.wolfcore.core.getPlugin(core.class);
        core.getLogger().info("Command: " + command.name);
        core.getLogger().info("Command: " + command.node);
        command.options.forEach(option -> {
            core.getLogger().info("Option: " + option.name);
            core.getLogger().info("Optional: " + option.optional);
            if (!option.optional) {
                result.set(0, result.get(0) + 1);
            }
        });
        return result.get(0);
    }
    default String checkArgs(String[] args, String alias, core core) {
        Command command = getCommand();
        Integer requiredArgs = requiredArgs();
        core.getLogger().info("Required args: " + requiredArgs);
        core.getLogger().info("Args length: " + args.length);
        if (args.length < requiredArgs) {
            return getUsage(alias);
        } else if (args.length > command.options.size()) {
            return "<#ffaa00>Error: <#ff5555>Too many args";
        }
        return null;
    }
    @Override
    default boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        core core = risingdeathx2.spigot.wolfcore.core.getPlugin(core.class);
        String result = checkArgs(args, label, core);
        if (result != null) {
            utils.sendColorText(core.adventure().sender(sender), result);
            return false;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission(getCommand().node)) {
                utils.sendColorText(core.adventure().sender(sender), core.getMessage("generic.nopermission"));
                return false;
            }
        }
        return execute(sender, command, label, args);
    }
    boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args);
}
