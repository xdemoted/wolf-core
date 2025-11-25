package com.wolfco.main.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.entity.Player;

import com.wolfco.common.Utilities;
import com.wolfco.main.Core;

import net.luckperms.api.cacheddata.CachedDataManager;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;

public class FontUtil {

    static public String createNameTag(String name, String textColor) {
        StringBuilder sb = new StringBuilder();
        String colorStart = "<#" + textColor + ">";
        String colorEnd = "</#" + textColor + ">";
        char[] chars = name.toCharArray();

        sb.append("<glyph:").append(((Core) Core.get()).getServerName().toLowerCase()).append(":c>");

        for (int i = 0; i < chars.length; i++) {
            String letter = String.valueOf(chars[i]).toLowerCase();
            sb.append("<shift:-2><glyph:").append(letter).append(":c>");
        }

        sb.append("<shift:-2><glyph:end:c>");

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

    static public String getPlayerTag(Player player) {
        Core core = (Core) Core.get();
        User user = core.getLuckPerms().getUserManager().getUser(player.getUniqueId());
        CachedDataManager cacheData = user.getCachedData();
        CachedMetaData lpmetaData = cacheData.getMetaData();
        String prefix = Utilities.nullCheck(lpmetaData.getPrefix());

        if (prefix.contains(";")) {
            prefix = prefix.split(";")[0];
        }

        return parseNameTag(prefix + player.getName());
    }
}
