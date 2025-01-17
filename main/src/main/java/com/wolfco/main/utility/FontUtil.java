package com.wolfco.main.utility;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wolfco.main.Core;

public class FontUtil {
    static final Map<String, String> fontMap = new HashMap<>();
    static {
        fontMap.put("A", "ꑒ");
        fontMap.put("B", "ꑓ");
        fontMap.put("C", "ꑔ");
        fontMap.put("D", "ꑕ");
        fontMap.put("E", "ꑖ");
        fontMap.put("F", "ꑗ");
        fontMap.put("G", "ꑘ");
        fontMap.put("H", "ꑙ");
        fontMap.put("I", "ꑚ");
        fontMap.put("J", "ꑛ");
        fontMap.put("K", "ꑜ");
        fontMap.put("L", "ꑝ");
        fontMap.put("M", "ꑞ");
        fontMap.put("N", "ꑟ");
        fontMap.put("O", "ꑠ");
        fontMap.put("P", "ꑡ");
        fontMap.put("Q", "ꑢ");
        fontMap.put("R", "ꑣ");
        fontMap.put("S", "ꑤ");
        fontMap.put("T", "ꑥ");
        fontMap.put("U", "ꑦ");
        fontMap.put("V", "ꑧ");
        fontMap.put("W", "ꑨ");
        fontMap.put("X", "ꑩ");
        fontMap.put("Y", "ꑪ");
        fontMap.put("Z", "ꑫ");
    }

    static public String createNameTag(String name, String textColor) {
        StringBuilder sb = new StringBuilder();
        String colorStart = "<#" + textColor + ">";
        String colorEnd = "</#" + textColor + ">";
        char[] chars = name.toCharArray();

        sb.append("ꑬ");

        for (int i = 0; i < chars.length; i++) {
            if (i == chars.length - 1) sb.append("ꑭ");
            else sb.append("ꑑ");

            String letter = String.valueOf(chars[i]);

            if (fontMap.containsKey(letter.toUpperCase())) {
                sb.append(colorStart).append(fontMap.get(letter.toUpperCase())).append(colorEnd);
            } else {
                sb.append(letter);
            }
        }

        return sb.toString();
    }

    static public String parseNameTag(String message) { // <nametag:000000>name
        Core core = (Core) Core.get();

        Pattern regex = Pattern.compile("<nametag:[0-9A-Fa-f]{6}>.*?</nametag>");
        Matcher matcher = regex.matcher(message);

        while (matcher.find()) {
            String tag = matcher.group();
            core.log(tag);
            String color = tag.substring(9, 15);
            core.log(color);
            String name = tag.substring(16, tag.length() - 10);
            core.log(name);
            message = message.replace(tag, createNameTag(name, color));
            core.log(message);
        }

        return message;
    }
}
