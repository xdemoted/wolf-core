package com.wolfco.main.handlers;

import java.util.Set;
import java.util.SortedMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import com.wolfco.common.Utilities;
import com.wolfco.common.listeners.CoreListener;
import com.wolfco.main.Core;

import jakarta.inject.Named;
import jakarta.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;

@Named("PlrVisualHandler")
@Singleton
public class PlayerVisualsHandler implements CoreListener {
    final MiniMessage miniMessage = MiniMessage.miniMessage();
    private Boolean tempTeams = false;

    LuckPerms lp;
    Core core;

    public PlayerVisualsHandler(Core core) {
        this.lp = core.getLuckPerms();
        this.core = core;
    }

    public void updateTeams() {
        lp.getGroupManager().loadAllGroups().thenAccept(result -> {
            lp.getGroupManager().getLoadedGroups().forEach(group -> {
                updateGroup(group);
            });
        });
    }

    public void updateGroup(Group group) {
        ScoreboardManager manager = core.getServer().getScoreboardManager();
        SortedMap<Integer, String> prefixes = group.getCachedData().getMetaData().getPrefixes();

        if (prefixes.isEmpty()) {
            return;
        } else if (prefixes.size() > 1) {
            core.log("Group " + group.getName() + " has multiple prefixes. Skipping.");
            return;
        }

        String prefix = prefixes.values().iterator().next();

        Component formattedPrefix = Utilities.formatPrefixString(prefix);

        Set<Team> teams = manager.getMainScoreboard().getTeams();

        teams.forEach(team -> {
            if (team.displayName().equals(Component.text("WolfCoreTeam")) && team.getName().equals(group.getName())) {
                team.prefix(miniMessage.deserialize(prefix));
            } else {
                Team newTeam = manager.getMainScoreboard().registerNewTeam(group.getName());
                newTeam.prefix(formattedPrefix);
                newTeam.displayName(Component.text("WolfCoreTeam"));
            }
        });
    }

    public void updatePrefix(Player player) {
        core.getLuckPerms().getUserManager().loadUser(player.getUniqueId()).thenAccept(user -> {
            Component prefix = Utilities.getPrefix(user);

            ScoreboardManager manager = player.getServer().getScoreboardManager();

            Set<Team> teams = manager.getMainScoreboard().getTeams();

            teams.forEach(team -> {
                if (team.displayName().equals(Component.text("WolfCoreTeam")) && team.prefix().equals(prefix)) {
                    team.addEntry(player.getName());
                } else {
                    core.log("Unknown prefix from " + player.getName());
                    Team newTeam = manager.getMainScoreboard().registerNewTeam(player.getName());
                    newTeam.prefix(prefix);
                    newTeam.displayName(Component.text("WolfCoreTeamTemp"));
                    newTeam.addEntry(player.getName());
                    tempTeams = true;
                }
            });
        });
    }

    @EventHandler
    public void onPlayerQuit(org.bukkit.event.player.PlayerQuitEvent event) {
        if (tempTeams) {
            AtomicInteger tempTeamCount = new AtomicInteger(0);
            Player player = event.getPlayer();
            ScoreboardManager manager = player.getServer().getScoreboardManager();

            Set<Team> teams = manager.getMainScoreboard().getTeams();

            teams.forEach(team -> {
                if (team.displayName().equals(Component.text("WolfCoreTeamTemp"))) {
                    team.removeEntry(player.getName());
                    if (team.getEntries().isEmpty()) {
                        team.unregister();
                        tempTeamCount.getAndDecrement();
                    } else {
                        tempTeamCount.getAndIncrement();
                    }
                }
            });
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        updatePrefix(event.getPlayer());
    }
}
