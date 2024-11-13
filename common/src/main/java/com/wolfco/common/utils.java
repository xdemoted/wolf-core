package com.wolfco.common;

import java.util.Map;

import org.bukkit.entity.Player;

import com.wolfco.common.classes.ArgumentInterface;
import com.wolfco.common.classes.ArgumentType;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CorePlugin;
import com.wolfco.common.classes.Subcommand;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.cacheddata.CachedPermissionData;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

public class utils {
    CorePlugin core;

    public utils(CorePlugin core) {
        this.core = core;
    }

    public static String[] gamemodes = { "survival", "creative", "adventure", "spectator" };

    public static void sendColorText(Audience audience, String text) {
        audience.sendMessage(MiniMessage.miniMessage().deserialize(text));
    }

    @SuppressWarnings("unchecked")
    public Collection<Player> getTargets(String target) {
        core.getLogger().info('"' + target + '"');
        core.getLogger().info(String.valueOf(target.equalsIgnoreCase("*")));
        if (target.equalsIgnoreCase("*")) {
            return (Collection<Player>) core.getServer().getOnlinePlayers();
        } else {
            List<Player> targetList = new ArrayList<>();
            Player targetPlayer = getTarget(target);
            if (targetPlayer != null) {
                targetList.add(targetPlayer);
            }
            return targetList;
        }
    }

    public Player getTarget(String target) {
        Collection<? extends Player> players = core.getServer().getOnlinePlayers();
        if (target.length() < 3)
            return null;
        for (Player player : players) {
            if (player.getName().toLowerCase().startsWith(target.toLowerCase()))
                return player;
        }
        return null;
    }

    public static String nullCheck(String text) {
        if (text == null) {
            return "";
        } else {
            return text;
        }
    }

    public static String[] getMetaData(CachedPermissionData data, Player player) {
        Map<String, Boolean> permissionMap = data.getPermissionMap();
        String[] returnData = { "", "" };
        Integer[] weight = { 0, 0 };
        permissionMap.forEach((k, v) -> {
            if (k.startsWith("wolf-co.chatprefix") && v) {
                String[] parts = k.split("\\.");
                Integer kweight = 0;
                try {
                    kweight = Integer.parseInt(parts[2]);
                } catch (NumberFormatException e) {
                    kweight = 0;
                }
                if (kweight > weight[0]) {
                    weight[0] = kweight;
                    returnData[0] = "";
                    for (int i = 3; i < parts.length; i++) {
                        returnData[0] += parts[i];
                    }
                }
            } else if (k.startsWith("wolf-co.chatsuffix") && v) {
                String[] parts = k.split("\\.");
                Integer kweight = 0;
                try {
                    kweight = Integer.parseInt(parts[2]);
                } catch (NumberFormatException e) {
                    kweight = 0;
                }
                if (kweight > weight[1]) {
                    weight[1] = kweight;
                    returnData[1] = "";
                    for (int i = 3; i < parts.length; i++) {
                        returnData[1] += parts[i];
                    }
                }
            }
        });
        return returnData;
    }

    static public ArgumentInterface getArgument(List<ArgumentInterface> arguments, List<String> args, int i) {
        Command command;

        while (i >= arguments.size()) {
            if (arguments.getLast().getType() == ArgumentType.SUBCOMMAND) {
                Subcommand subcommand = (Subcommand) arguments.getLast();
                command = subcommand.get(args.get(arguments.size() - 1));
                if (command == null) {
                    return null;
                } else {
                    i -= arguments.size();
                    arguments = command.options;
                }
            } else {
                return null;
            }
        }

        return arguments.get(i);
    }
}
