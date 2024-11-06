package com.wolfco.velocity;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import java.util.Collection;
import java.util.Arrays;

import com.velocitypowered.api.proxy.Player;

import com.wolfco.velocity.types.OfflinePlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
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
    public static Component parseColors(String message) {
        TextColor color = TextColor.fromHexString("#ffffff");
        String gradientStart = null;
        Boolean bold = false;
        Boolean italic = false;
        Boolean underlined = false;
        Boolean strikethrough = false;
        Boolean obfuscated = false;
        Component component = Component.empty();
        String current = "";
        String[] messageChars = message.split("");
        for (int i = 0; i < messageChars.length; i++) {
            String c = messageChars[i];
            if (c.equals("§") && i < messageChars.length - 1) {
                String next = messageChars[i + 1];
                component = append(component, color, bold, italic, underlined, strikethrough, obfuscated, current);
                switch (next) {
                    case "l":
                        bold = !bold;
                        break;
                    case "k":
                        obfuscated = !obfuscated;
                        break;
                    case "n":
                        underlined = !underlined;
                        break;
                    case "m":
                        strikethrough = !strikethrough;
                        break;
                    case "o":
                        italic = !italic;
                        break;
                    case "p":
                        italic = false;
                        bold = false;
                        underlined = false;
                        strikethrough = false;
                        obfuscated = false;
                        break;
                    case "r":
                        gradientStart = null;
                        bold = false;
                        italic = false;
                        underlined = false;
                        strikethrough = false;
                        obfuscated = false;
                        color = TextColor.fromHexString("#ffffff");
                        break;
                    default:
                        continue;
                }
                i++;
                current = "";
            } else if (i <= messageChars.length - 9 && message.substring(i, i + 9).matches("<¶([0-9a-fA-F]{6})>")) { // <#ffffff>
                gradientStart = null;
                component = append(component, color, bold, italic, underlined, strikethrough, obfuscated, current);
                color = TextColor.fromHexString("#" + message.substring(i + 2, i + 8));
                current = "";
                i += 8;
            } else if (i <= messageChars.length - 10 && message.substring(i, i + 10).matches("<g¶[a-fA-F0-9]{6}>")) { // <g§ffffff>
                if (gradientStart == null) {
                    component = append(component, color, bold, italic, underlined, strikethrough, obfuscated, current);
                    gradientStart = message.substring(i + 3, i + 9);
                    color = TextColor.fromHexString("#" + gradientStart);
                } else {
                    int[] startRGB = utils.toRGB(gradientStart);
                    int[] endRGB = utils.toRGB(message.substring(i + 3, i + 9));
                    int[] currentRGB = startRGB.clone();
                    String[] gradientChars = current.split("");
                    for (int j = 0; j < gradientChars.length; j++) {
                        String gradientChar = gradientChars[j];
                        if (gradientChar.equals(" ")) {
                            component = component.append(Component.text(" "));
                            continue;
                        }
                        component = append(component, TextColor.fromHexString("#"+utils.toHex(currentRGB)), bold, italic, underlined, strikethrough, obfuscated, current);
                        for (int k = 0; k < startRGB.length; k++) {
                            currentRGB[k] = Math.round(((endRGB[k] - startRGB[k]) / gradientChars.length)*j + startRGB[k]);
                        }
                    }
                    color = TextColor.fromHexString("#" + utils.toHex(currentRGB));
                    gradientStart = null;
                }
                current = "";
                i += 9;
            } else {
                current += c;
            }
        }
        return append(component, color, bold, italic, underlined, strikethrough, obfuscated, current);
    }
    public static Component append(Component component, TextColor color, Boolean bold, Boolean italic, Boolean underlined, Boolean strikethrough, Boolean obfuscated, String text) {
        Component builder = Component.text(text)
            .color(color);
        if (bold) {
            builder = builder.decorate(TextDecoration.BOLD);
        }
        if (italic) {
            builder = builder.decorate(TextDecoration.ITALIC);
        }
        if (underlined) {
            builder = builder.decorate(TextDecoration.UNDERLINED);
        }
        if (strikethrough) {
            builder = builder.decorate(TextDecoration.STRIKETHROUGH);
        }
        if (obfuscated) {
            builder = builder.decorate(TextDecoration.OBFUSCATED);
        }
        return component.append(builder);
    }
    public static HashMap<String, String> colorCodes = new HashMap<String, String>();
    {
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
    public static String colorizeText(String text) {
        String[] textChar = text.split("");
        for (int i = 0; i < textChar.length; i++) {
            String c = textChar[i];
            if (c.equals("&") && i < textChar.length - 1) {
                String next = textChar[i + 1];
                if (utils.colorCodes.containsKey(next)) {
                    textChar[i] = "<¶" + utils.colorCodes.get(next) + ">";
                    textChar[i + 1] = "";
                }
            }
        }
        text = String.join("", textChar);
        text = text.replaceAll("&([kmolnrp])", "§$1");
        text = text.replaceAll("<g#([0-9a-fA-F]{6})>", "<g¶$1>");
        return text.replaceAll("<#([0-9a-fA-F]{6})>", "<¶$1>");
    }
    public static String nullCheck(String text) {
        if (text == null) {
            return "";
        } else {
            return text;
        }
    }
    public static int[] toRGB(String hex) {
        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);
        return new int[] { r, g, b };
    }
    public static String toHex(int[] rgb) {
        return String.format("%02x%02x%02x", rgb[0], rgb[1], rgb[2]);
    }
}
