package com.wolfco.main.whitelist;

import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.wolfco.main.Core;

import dev.dejvokep.boostedyaml.YamlDocument;
import jakarta.inject.Singleton;

@Singleton
public class WhitelistManager {
    private final String WHITELIST_BYPASS_PERMISSION = "wolfcore.whitelist.bypass";

    private final Core core;

    private String WHITELIST_PERMISSION;
    private int mode; // 0: off, 1: permission, 2: list, 3: bypass only
    private Boolean updateFile = true; // Failure to load: don't update file to prevent overwriting
    private final List<UUID> whitelistedUUIDs;

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
            whitelistedUUIDs = List.of();
            return;
        }

        if (whitelist != null) {
            whitelistedUUIDs = whitelist.getStringList("whitelist", List.of()).stream()
                    .map(UUID::fromString)
                    .toList();
            mode = whitelist.getInt("mode", 0);
        } else {
            core.log("Enforcing strict whitelist due to error loading whitelist.yml");

            whitelistedUUIDs = List.of();
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

    public int addToWhitelist(UUID playerUUID) { // 0: ok 1: already whitelisted 2: failed to save
        if (whitelistedUUIDs.contains(playerUUID)) {
            return 1;
        }

        whitelistedUUIDs.add(playerUUID);

        return saveWhitelist() ? 0 : 2;
    }

    public int removeFromWhitelist(UUID playerUUID) { // 0: ok 1: not whitelisted 2: failed to save
        if (!whitelistedUUIDs.contains(playerUUID)) {
            return 1;
        }

        whitelistedUUIDs.remove(playerUUID);

        return saveWhitelist() ? 0 : 2;
    }

    public boolean saveWhitelist() {
        if (updateFile) {
            YamlDocument whitelist = core.getConfigDocument("whitelist.yml");

            if (whitelist == null) {
                core.log("Failed to load whitelist.yml");
                return false;
            }

            whitelist.set("whitelist", whitelistedUUIDs.stream()
                    .map(UUID::toString)
                    .toList());
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
                return whitelistedUUIDs.contains(player.getUniqueId()) ? 1 : 0;
            }
        }

        return 0;
    }
}
