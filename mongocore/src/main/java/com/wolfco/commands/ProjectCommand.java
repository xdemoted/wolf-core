package com.wolfco.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.wolfco.Main;
import com.wolfco.MongoConnector;
import com.wolfco.Utils;
import com.wolfco.types.Project;
import com.wolfco.types.State;

public class ProjectCommand implements CommandExecutor, TabCompleter {
    static final List<String> subCommands = List.of("create", "delete", "list", "set", "info");

    MongoConnector mongoConnector;
    Main main;

    public ProjectCommand(Main main) {
        this.main = main;
        this.mongoConnector = main.getMongoConnector();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Usage: /project <create|delete|list|modify>");
            return false;
        }

        boolean result = switch (args[0]) {
            case "create" -> create(sender, command, label, args);
            case "delete" -> delete(sender, command, label, args);
            case "list" -> list(sender, command, label, args);
            case "set" -> set(sender, command, label, args);
            case "info" -> info(sender, command, label, args);
            default -> false;
        };

        return result;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> options = List.of();
        if (args.length == 1) {
            options = subCommands;
        } else if (args.length == 2 && subCommands.contains(args[0])) {
            options = switch (args[0]) {
                case "create" -> List.of();
                case "delete" -> mongoConnector.listProjects().stream().map(Project::getName).toList();
                case "list" -> Arrays.stream(State.values()).map(Enum::name).toList();
                case "set" -> List.of(); // #TODO Set Command needs implementation.
                case "info" -> List.of();
                default -> List.of();
            };
        }

        return options.stream()
                .filter(option -> option.startsWith(args[args.length - 1]))
                .toList();
    }

    boolean create(CommandSender sender, Command command, String label, String[] args) {
        Project project = new Project();

        if (args.length > 1) {
            project.setName(args[1]);
        }

        if (args.length > 2) { // Add description
            project.setDescription(String.join(" ", Arrays.copyOfRange(args, 2, args.length)));
        }

        if (sender instanceof Player player) {
            project.setLocations(Utils.serializeLocation(player.getLocation()));
        }

        project.setState(State.NOTSTARTED);

        mongoConnector.addProject(project);

        return false;
    }

    boolean delete(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }

    boolean list(CommandSender sender, Command command, String label, String[] args) {

        main.sendMessage(sender, "Projects");

        switch (args.length) {
            case 1 -> {
                List<Project> projects = mongoConnector.listProjects();

                for (Project p : projects) {
                    main.sendMessage(sender, "<#ffaa00> - <#ffff00><click:run_command:/project info " + p.getName()
                            + "><hover:show_text:'<orange>Click for more info.'>" + p.getName() + "</hover></click>");
                }

                return true;
            }

            case 2 -> {
                final State state;

                try {
                    state = State.valueOf(args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage("Invalid state provided.");
                    return false;
                }

                List<Project> projects = mongoConnector.listProjects();

                if (state != null) {
                    projects.removeIf(p -> p.getState() != state);
                }

                for (Project p : projects) {
                    main.sendMessage(sender, "<#ffaa00> - <#ffff00><click:run_command:/project info " + p.getName()
                            + "><hover:show_text:'<orange>Click for more info.'>" + p.getName() + "</hover></click>");
                }

                return true;
            }

            default -> sender.sendMessage("Usage: /project list [state]");
        }

        return false;
    }

    boolean set(CommandSender sender, Command command, String label, String[] args) {
        return false; // #TODO Unimplemented Subcommands
    }

    boolean info(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }

}
