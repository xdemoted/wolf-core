package com.wolfco.main.whitelist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.wolfco.main.Core;

import dev.dejvokep.boostedyaml.YamlDocument;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class WhitelistManager {
    private final String WHITELIST_BYPASS_PERMISSION = "wolfcore.whitelist.bypass";

    private final Core core;

    private String WHITELIST_PERMISSION;
    private int mode; // 0: off, 1: permission, 2: list, 3: bypass only
    private Boolean updateFile = true; // Failure to load: don't update file to prevent overwriting
    private final HashMap<String, UUID> whitelistedUsers;

    @Inject
    public WhitelistManager(Core core) {
        this.core = core;
        YamlDocument whitelist;

        WHITELIST_PERMISSION = "wolfcore.whitelist." + core.getServerName().toLowerCase();

        try {
            whitelist = core.getConfigDocument("whitelist.yml");
        } catch (Exception e) {
            core.log("Enforcing strict whitelist due to error loading whitelist.yml");

            mode = 3;
            updateFile = false;
            whitelistedUsers = new HashMap<>();
            return;
        }

        if (whitelist != null) {
            whitelistedUsers = retrieveUsers(whitelist);
            mode = whitelist.getInt("mode", 0);
        } else {
            core.log("Enforcing strict whitelist due to error loading whitelist.yml");

            whitelistedUsers = new HashMap<>();
            mode = 3;
            updateFile = false;
        }
    }

    public int getMode() {
        return mode;
    }

    public boolean setMode(int mode) {
        this.mode = mode;
        return saveWhitelist();
    }

    public int addToWhitelist(String name, UUID playerUUID) { // 0: ok 1: already whitelisted 2: failed to save
        if (whitelistedUsers.containsValue(playerUUID)) {
            return 1;
        }

        whitelistedUsers.put(name, playerUUID);

        return saveWhitelist() ? 0 : 2;
    }

    public int removeFromWhitelist(String name, UUID playerUUID) { // 0: ok 1: not whitelisted 2: failed to save
        if (!whitelistedUsers.containsValue(playerUUID)) {
            return 1;
        }

        whitelistedUsers.remove(name, playerUUID);

        return saveWhitelist() ? 0 : 2;
    }

    public List<String> getWhitelistedNames() {
        return whitelistedUsers.keySet().stream().toList();
    }

    public boolean saveWhitelist() {
        if (updateFile) {
            YamlDocument whitelist = core.getConfigDocument("whitelist.yml");

            if (whitelist == null) {
                core.log("Failed to load whitelist.yml");
                return false;
            }

            List<String> whitelistedList = new ArrayList<>();

            whitelistedUsers.forEach((username, uuid) -> {
                String userString = uuid.toString() + "|" + username;
                whitelistedList.add(userString);
            });

            whitelist.set("mode", mode);
            whitelist.set("whitelist", whitelistedList);
            try {
                whitelist.save();
            } catch (Exception e) {
                core.log("Failed to save whitelist.yml");
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    public final HashMap<String, UUID> retrieveUsers(YamlDocument document) {
        HashMap<String, UUID> users = new HashMap<>();

        List<String> whitelistedList = document.getStringList("whitelist", List.of());

        for (String userString : whitelistedList) {
            String[] parts = userString.split("\\|");
            if (parts.length == 2) {
                UUID uuid = UUID.fromString(parts[0]);
                String username = parts[1];
                users.put(username, uuid);
            }
        }

        return users;
    }

    public int isWhitelisted(Player player) { // Returns 2 for bypass, 1 for whitelisted, 0 for not whitelisted
        if (mode == 0) {
            return 1;
        } else if (player.hasPermission(WHITELIST_BYPASS_PERMISSION)) {
            return 2;
        }

        switch (mode) {
            case 1 -> {
                return player.hasPermission(WHITELIST_PERMISSION) ? 1 : 0;
            }
            case 2 -> {
                return whitelistedUsers.containsValue(player.getUniqueId()) ? 1 : 0;
            }
        }

        return 0;
    }
}
