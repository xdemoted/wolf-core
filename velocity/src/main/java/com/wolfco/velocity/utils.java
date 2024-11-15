package com.wolfco.velocity;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.velocitypowered.api.proxy.Player;
import com.wolfco.velocity.types.OfflinePlayer;

import net.luckperms.api.model.user.User;

public class utils {
    public static long[] quotientRemainder(long dividend, long divisor) {
        long quotient = dividend / divisor;
        long remainder = dividend % divisor;
        return new long[] { quotient, remainder };
    }
    public static String time(long start) {
        return time(start, System.currentTimeMillis());
    }
    public static String time(long start, long end) {
        long time = Math.round(end - start);
        long[] years = quotientRemainder(time, 31556952000L);
        long[] months = quotientRemainder(years[1], 2629743L);
        long[] days = quotientRemainder(months[1], 86400L);
        long[] hours = quotientRemainder(days[1], 3600L);
        long[] minutes = quotientRemainder(hours[1], 60L);
        long seconds = minutes[1];
        String returnString = "";
        if (years[0] > 0) {
            returnString += years[0] + "Years ";
        } else if (months[0] > 0) {
            returnString += months[0] + "Months ";
        } else if (days[0] > 0) {
            returnString += days[0] + "Days ";
        } else if (hours[0] > 0) {
            returnString += hours[0] + "Hours ";
        } else if (minutes[0] > 0) {
            returnString += minutes[0] + "Minutes ";
        } else {
            returnString += seconds + "Seconds ";
        }
        return returnString;
    }
    public static <T> Collection<T> convertArray(T[] array) {
        return Arrays.asList(array);
    }
    public static String joinCollection(Collection<String> collection) {
        return joinCollection(collection, "");
    }
    public static String joinCollection(Collection<String> collection, String separator) {
        return collection.stream().collect(StringBuilder::new, (sb, s) -> {
            if (sb.length() > 0) {
                sb.append(separator);
            }
            sb.append(s);
        }, StringBuilder::append).toString();
    }
    public static String displayname(Player player,wolfcore plugin) {
        User user = plugin.lp.getPlayerAdapter(Player.class).getUser(player);
        String prefix = user.getCachedData().getMetaData().getPrefix();
        OfflinePlayer playerData = plugin.playerManager.getOfflinePlayer(player.getUniqueId());
        if (prefix == null) {
            prefix = "";
        }
        return (prefix + playerData.nickname()).replace('&', '§');
    }
    public static CompletableFuture<String> getOfflineDisplay(UUID uuid,wolfcore plugin) {
        CompletableFuture<User> future = plugin.lp.getUserManager().loadUser(uuid);
        User user = future.join();
        OfflinePlayer offlinePlayer = plugin.playerManager.getOfflinePlayer(uuid);
        String prefix = user.getCachedData().getMetaData().getPrefix();
        if (prefix == null) {
            prefix = "";
        }
        final String finalPrefix = prefix;
        return CompletableFuture.supplyAsync(() -> (finalPrefix + offlinePlayer.nickname()).replace('&', '§'));
    }
    public static String nullCheck(String text) {
        if (text == null) {
            return "";
        } else {
            return text;
        }
    }
}
