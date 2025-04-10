package com.wolfco.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.entity.Player;

import com.wolfco.common.classes.CorePlugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.cacheddata.CachedPermissionData;

public class Utilities {

    public final static String[] gamemodes = { "survival", "creative", "adventure", "spectator" };

    public final static HashMap<String, String> colorCodes = new HashMap<>();
    static {
        colorCodes.put("0", "000000");
        colorCodes.put("1", "0000AA");
        colorCodes.put("2", "00AA00");
        colorCodes.put("3", "00AAAA");
        colorCodes.put("4", "AA0000");
        colorCodes.put("5", "AA00AA");
        colorCodes.put("6", "FFAA00");
        colorCodes.put("7", "AAAAAA");
        colorCodes.put("8", "555555");
        colorCodes.put("9", "5555FF");
        colorCodes.put("a", "55FF55");
        colorCodes.put("b", "55FFFF");
        colorCodes.put("c", "FF5555");
        colorCodes.put("d", "FF55FF");
        colorCodes.put("e", "FFFF55");
        colorCodes.put("f", "FFFFFF");
    }

    @SuppressWarnings("unchecked")
    static public Collection<Player> getTargets(CorePlugin core, String target) {
        if (target.equalsIgnoreCase("*")) {
            return (Collection<Player>) core.getServer().getOnlinePlayers();
        } else {
            List<Player> targetList = new ArrayList<>();
            Player targetPlayer = getTarget(core, target);
            if (targetPlayer != null) {
                targetList.add(targetPlayer);
            }
            return targetList;
        }
    }

    static public Player getTarget(CorePlugin core, String target) {
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
                Integer kweight;
                try {
                    kweight = Integer.valueOf(parts[2]);
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
                Integer kweight;
                try {
                    kweight = Integer.valueOf(parts[2]);
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

    public static Component colorizeText(String string) {
        return MiniMessage.miniMessage().deserialize(string);
    }

    public static double getRandomDouble(double min, double max) {
        return Math.random() * (max - min) + min;
    }

    public static <T> T getRandomValue(List<T> list) {
        return list.get(new Random().nextInt(list.size()));
    }
}
