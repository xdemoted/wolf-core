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
    static public String createNameTag(String text) {
        StringBuilder sb = new StringBuilder();
        char[] chars = text.toCharArray();

        sb.append("<glyph:").append(((Core) Core.get()).getIcon()).append(":c>");

        for (int i = 0; i < chars.length; i++) {
            String letter = String.valueOf(chars[i]).toLowerCase();
            sb.append("<shift:-2><glyph:").append(letter).append(":c>");
        }

        sb.append("<shift:-2><glyph:end:c>");

        return sb.toString();
    }

    static public String parseNameTag(String message) { // <nametag>name</nametag>

        Pattern regex = Pattern.compile("<nametag>.*?</nametag>");
        Matcher matcher = regex.matcher(message);

        while (matcher.find()) {
            String tag = matcher.group();
            String name = tag.substring(9, tag.length() - 10);
            message = message.replace(tag, createNameTag(name));
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
