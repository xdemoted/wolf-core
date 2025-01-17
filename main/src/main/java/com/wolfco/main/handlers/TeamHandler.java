package com.wolfco.main.handlers;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import com.wolfco.main.Core;
import com.wolfco.main.utility.ColorUtil;
import com.wolfco.main.utility.FontUtil;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.luckperms.api.LuckPerms;

public class TeamHandler {
    final MiniMessage miniMessage = MiniMessage.miniMessage();
    LuckPerms lp;
    Core core;

    public TeamHandler(Core core) {
        this.lp = core.getLuckPerms();
        this.core = core;

        //EventBus eventBus = lp.getEventBus();
    }

    public void updatePrefix(Player player) {
        core.log("Updating prefix for " + player.getName());

        lp.getUserManager().loadUser(player.getUniqueId()).thenAccept(user -> {
            String prefix = user.getCachedData().getMetaData().getPrefix();

            if (prefix == null)
                return;

            prefix = FontUtil.parseNameTag(prefix);

            String formattedPrefix = LegacyComponentSerializer.legacy('§').serialize(miniMessage.deserialize(prefix));
            ChatColor color = ColorUtil.parseCode(ChatColor.getLastColors(formattedPrefix));

            ScoreboardManager manager = player.getServer().getScoreboardManager();

            if (manager == null)
                return;

            Team existingTeam = manager.getMainScoreboard().getTeam(player.getName());

            if (existingTeam != null) {
                existingTeam.unregister();
            }

            Set<Team> teams = manager.getMainScoreboard().getTeams();

            teams.forEach(team -> {
                core.log(team.getName());

                if (team.getDisplayName().equals("WolfCoreTeam") && team.getPrefix().equals(formattedPrefix)) {
                    team.addEntry(player.getName());
                } else {
                    Team newTeam = manager.getMainScoreboard().registerNewTeam(player.getName());
                    newTeam.setPrefix(formattedPrefix);
                    newTeam.setColor(color);
                    newTeam.setDisplayName("WolfCoreTeam");
                    newTeam.addEntry(player.getName());
                }
            });
        });
    }
}
