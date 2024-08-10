package risingdeathx2.spigot.wolfcore;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import net.kyori.adventure.audience.Audience;
import risingdeathx2.spigot.wolfcore.commands.db;
import risingdeathx2.spigot.wolfcore.commands.gamemode;
import risingdeathx2.spigot.wolfcore.commands.teleport;

import java.util.ArrayList;
import java.util.List;

public class command implements CommandExecutor {
    core plugin;

    public command(core core) {
        plugin = core;
        List<String> commands = new ArrayList<String>();
        commands.add("db");
        commands.add("help");
        commands.add("mute");
        commands.add("lag");
        commands.add("delhome");
        commands.add("sethome");
        commands.add("home");
        commands.add("kickall");
        commands.add("kick");
        commands.add("whois");
        commands.add("tptoggle");
        commands.add("gamemode");
        commands.add("tpa");
        commands.add("tpahere");
        commands.add("tpaccept");
        commands.add("tpdeny");
        commands.add("teleport");
        commands.add("tppos");
        commands.add("setwarp");
        commands.add("delwarp");
        commands.add("warp");
        commands.add("warpinfo");
        commands.add("ban");
        commands.add("unban");
        commands.add("banlist");
        commands.add("tempban");
        commands.add("tempmute");
        commands.add("unmute");
        commands.add("banip");
        commands.add("unbanip");
        commands.add("tempbanip");
        registerCommands(commands);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        Player player = sender.getServer().getPlayer(sender.getName());
        String cname = command.getName();
        Audience audience = plugin.adventure().player(player);
        if (alias.equalsIgnoreCase("db")) {
            new db(plugin, args);
        } else if (alias.equalsIgnoreCase("help")) {
            utils.sendColorText(audience, "<#ffaa00>" + core.prefix + "<Insert helpful message here>");
        } else if (cname.equalsIgnoreCase("gamemode")) {
            new gamemode(plugin, player, alias, args);
        } else if (cname.equalsIgnoreCase("teleport")) {
            new teleport(plugin, player, alias, args);
        }

        return false;
    }

    public void registerCommands(List<String> commands) {
        for (String command : commands) {
            PluginCommand pCommand = plugin.getCommand(command);
            if (pCommand != null)
                pCommand.setExecutor(this);
            else
                plugin.getLogger().warning("Command " + command + " not found.");
        }
    }
}