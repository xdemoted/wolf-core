package com.wolfco.common.classes;

import java.util.List;

import org.bukkit.command.CommandSender;

public interface ArgumentInterface {
    default IllegalArgumentException error(String message, Object... args) { // Convert string array into formattable segments
        String[] parts = message.split("%");
        StringBuilder formattedMessage = new StringBuilder();

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];

            if (part.isBlank()) {
                continue;
            }
            if (i >= args.length) {
                break;
            }

            formattedMessage.append(String.format(part, args[i]));
        }

        message = formattedMessage.toString();
        return new IllegalArgumentException(message);
    }

    abstract Boolean isRequired();

    abstract String getName();

    abstract ArgumentInterface setName(String name);

    abstract List<String> getOptions(CorePlugin core, CommandSender sender, org.bukkit.command.Command bukkitCommand, String[] args);

    abstract Object getValue(CorePlugin core, CommandSender sender, org.bukkit.command.Command bukkitCommand, String searchValue) throws IllegalArgumentException;

}
