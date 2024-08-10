package risingdeathx2.spigot.wolfcore;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.luckperms.api.cacheddata.CachedPermissionData;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

public class utils {
    core core;
    public utils(core core) {
        this.core = core;
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
    @SuppressWarnings("unchecked")
    public Collection<Player> getTargets(String target) {
        core.getLogger().info('"'+target+'"');
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
    public void sendPlayer(Player player, String text) {
        sendColorText(core.adventure().player(player), text);
    }
    public static void sendColorText(Audience audience, String text) {
        text = colorizeText(text);
        audience.sendMessage(parseColors(text));
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
    public static String[] getMetaData(CachedPermissionData data,Player player) {
        Map<String, Boolean> permissionMap = data.getPermissionMap();
        String[] returnData = {"",""};
        Integer[] weight = {0,0};
        permissionMap.forEach((k, v) -> {
            if (k.startsWith("wolf-co.chatprefix")&&v) {
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
            } else if (k.startsWith("wolf-co.chatsuffix")&&v) {
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
}
