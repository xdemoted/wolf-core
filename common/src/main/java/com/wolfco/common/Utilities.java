package com.wolfco.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.wolfco.common.classes.CorePlugin;

import dev.dejvokep.boostedyaml.YamlDocument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.cacheddata.CachedDataManager;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.cacheddata.CachedPermissionData;
import net.luckperms.api.model.user.User;

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

    public static Vector getUnitLocation(Location from, Location to) {
        Location direction = to.clone().subtract(from);
        return direction.toVector().normalize(); // returns unit vector
    }

    public static Location loadLocation(YamlDocument document, String path) {
        String world = document.getString(path + ".world", "world");
        UUID worldUUID;

        try {
            worldUUID = UUID.fromString(world);
        } catch (IllegalArgumentException e) {
            return null;
        }

        double x = document.getDouble(path + ".x", 0d);
        double y = document.getDouble(path + ".y", 0d);
        double z = document.getDouble(path + ".z", 0d);

        float yaw = document.getFloat(path + ".yaw", 0f);
        float pitch = document.getFloat(path + ".pitch", 0f);

        World w = org.bukkit.Bukkit.getWorld(worldUUID);

        if (w == null) {
            return null;
        }

        Location location = new Location(org.bukkit.Bukkit.getWorld(worldUUID), x, y, z, yaw, pitch);

        return location;
    }

    static public String createNameTag(String text) {
        StringBuilder sb = new StringBuilder();
        char[] chars = text.toCharArray();

        sb.append("<glyph:").append((CorePlugin.get()).getIcon()).append(":c>");

        for (int i = 0; i < chars.length; i++) {
            String letter = String.valueOf(chars[i]).toLowerCase();
            sb.append("<shift:-2><glyph:").append(letter).append(":c>");
        }

        sb.append("<shift:-2><glyph:end:c>");

        return sb.toString();
    }

    static public String parseNameTag(String text) { // <nametag>name</nametag>
        Pattern regex = Pattern.compile("<nametag>.*?</nametag>");
        Matcher matcher = regex.matcher(text);

        while (matcher.find()) {
            String tag = matcher.group();
            String name = tag.substring(9, tag.length() - 10);
            text = text.replace(tag, createNameTag(name));
        }

        return text;
    }

    static public Component formatPrefixString(String prefix) {
        prefix = Utilities.nullCheck(prefix);

        if (prefix.contains(";")) {
            prefix = prefix.split(";")[0];
        }

        prefix = parseNameTag(prefix);

        return MiniMessage.miniMessage().deserialize(prefix);
    }

    static public Component getPrefix(User user) {
        CachedDataManager cacheData = user.getCachedData();
        CachedMetaData lpmetaData = cacheData.getMetaData();
        return formatPrefixString(lpmetaData.getPrefix());
    }

    static public Component getDisplayName(User user) {
        CachedDataManager cacheData = user.getCachedData();
        CachedMetaData lpmetaData = cacheData.getMetaData();
        String prefix = Utilities.nullCheck(lpmetaData.getPrefix());
        String suffix = Utilities.nullCheck(lpmetaData.getSuffix());
        Component formattedPrefix = formatPrefixString(prefix);

        return formattedPrefix.append(MiniMessage.miniMessage().deserialize(user.getUsername() + suffix));
    }

    static public CompletableFuture<UUID> getUUIDFromName(String name) {
        return CompletableFuture.supplyAsync(() -> {
            PlayerProfile profile = getMojangProfile(name).join();

            if (profile != null && profile.getId() != null) {
                return profile.getId();
            } else {
                return null;
            }
        });
    }

    static public CompletableFuture<PlayerProfile> getMojangProfile(String name) {
        return CompletableFuture.supplyAsync(() -> {
            PlayerProfile profile = Bukkit.createProfile(name);

            try {
                profile.complete(false);
                return profile;
            } catch (Exception e) {
                return null;
            }
        });
    }
}
