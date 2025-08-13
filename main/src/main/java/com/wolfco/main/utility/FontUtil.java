package com.wolfco.main.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wolfco.main.Core;

public class FontUtil {
    static public String createNameTag(String name, String textColor) {
        StringBuilder sb = new StringBuilder();
        String colorStart = "<#" + textColor + ">";
        String colorEnd = "</#" + textColor + ">";
        char[] chars = name.toCharArray();

        sb.append("¥");

        for (int i = 0; i < chars.length; i++) {
            if (i == chars.length - 1)
                sb.append("¢");
            else
                sb.append("¤");

            String letter = String.valueOf(chars[i]);
            
            sb.append(colorStart).append(letter).append(colorEnd);
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
