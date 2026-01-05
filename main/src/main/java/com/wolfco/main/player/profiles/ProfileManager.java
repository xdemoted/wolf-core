package com.wolfco.main.profiles;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.entity.Player;

import com.wolfco.main.Core;
import com.wolfco.main.profiles.classes.Profile;

import dev.dejvokep.boostedyaml.YamlDocument;
import jakarta.annotation.Nullable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class ProfileManager {
    private final Map<UUID, Profile> players = new HashMap<>();
    private final Core core;

    @Inject
    public ProfileManager(Core core) {
        this.core = core;
    }

    public Profile loadProfile(UUID uuid) {
        return loadProfile(uuid, false);
    }

    public Profile loadProfile(Player player) {
        return loadProfile(player.getUniqueId(), false);
    }

    public Profile loadOfflineProfile(UUID uuid) {
        return loadProfile(uuid, true);
    }

    public Profile loadOfflineProfile(Player player) {
        return loadProfile(player.getUniqueId(), true);
    }

    private Profile loadProfile(UUID uuid, boolean offline) {
        YamlDocument data = getProfileDocument(uuid);

        if (data == null) {
            core.log("[Wolf-Core] Player data not found for {0}", uuid);
            return null;
        }

        return new Profile(uuid, data, offline);
    }

    public Profile getCachedProfile(Player player) {
        return players.get(player.getUniqueId());
    }

    public boolean cacheProfile(Profile profile) {
        if (players.containsKey(profile.uuid)) {
            return false;
        }

        players.put(profile.uuid, profile);
        return true;
    }

    public boolean uncacheProfile(Profile profile) {
        if (!players.containsKey(profile.uuid)) {
            return false;
        }

        players.remove(profile.uuid);
        return true;
    }

    public List<reducedPlayerInfo> getAllProfiles() {
        File directory = Core.get().getDataFolder().toPath().resolve("userdata").toFile();
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                List<reducedPlayerInfo> documents = new ArrayList<>();
                for (File file : files) {
                    try {
                        UUID uuid = UUID.fromString(file.getName().replace(".yml", ""));
                        YamlDocument document = getProfileDocument(uuid);

                        if (document == null)
                            continue;

                        String username = document.getString("username", null);

                        if (username != null)
                            documents.add(new reducedPlayerInfo(uuid, username));
                    } catch (Exception e) {
                        Core.get().getLogger().log(Level.WARNING,
                                "[Wolf-Core] Could not load player data file: {0}", file.getName());
                    }
                }
                return documents;
            }
        }
        return List.of();
    }

    @Nullable
    public YamlDocument getProfileDocument(UUID uuid) {
        YamlDocument data = null;

        try {
            data = core.getConfigDocument(uuid.toString(),
                    core.getDataFolder().toPath().resolve("userdata"));
        } catch (Exception e) {
            core.getLogger().log(Level.WARNING, "[Wolf-Core] Player data not found for {0}", uuid.toString());
        }

        return data;
    }

    public static class reducedPlayerInfo {
        public UUID uuid;
        public String name;

        public reducedPlayerInfo(UUID uuid, String name) {
            this.uuid = uuid;
            this.name = name;
        }
    }
}