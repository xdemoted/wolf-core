package risingdeathx2.spigot.wolfcore;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import net.kyori.adventure.audience.Audience;
import risingdeathx2.spigot.wolfcore.classes.PlayerData;
import risingdeathx2.spigot.wolfcore.classes.request;
import risingdeathx2.spigot.wolfcore.commands.db;
import risingdeathx2.spigot.wolfcore.commands.gamemode;
import risingdeathx2.spigot.wolfcore.commands.teleport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class command implements CommandExecutor {
    core plugin;
    HashMap<String, String> messageAlias = new HashMap<String, String>();
    {
        messageAlias.put("tpa", "to teleport to you");
        messageAlias.put("tpahere", "that you teleport to them");
    }
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
        String cname = command.getName().toLowerCase();
        Audience audience = plugin.adventure().player(player);
        switch (cname) {
            case "db":
                new db(plugin, args);
                break;
            case "help":
                utils.sendColorText(audience, "<#ffaa00>" + core.prefix + "<Insert helpful message here>");
                break;
            case "gamemode":
                new gamemode(plugin, player, alias, args);
                break;
            case "teleport":
                new teleport(plugin, player, alias, args);
                break;
            case "tpahere":
            case "tpa":
                if (args.length == 1) {
                    Player target = plugin.utils.getTarget(args[0]);
                    if (target != null) {
                        if (target.getUniqueId() == player.getUniqueId()) {
                            utils.sendColorText(audience, "<#ffaa00>You can't teleport to yourself.");
                            return false;
                        }
                        PlayerData host = plugin.players.get(target.getUniqueId());
                        host.sendRequest(player, cname);
                        utils.sendColorText(audience, "<#ffaa00>Request sent to <#aa0000>" + target.getName()+"<#ffaa00>.\nRequest will expire in <#ff5555>30<#ffaa00> seconds.");
                        utils.sendColorText(plugin.adventure().player(target), "<#aa0000>"+player.getName()+"<#ffaa00> has requested " + messageAlias.get(cname) + ".\n<#ffaa00>Use <#ffff00>/tpaccept<#ffaa00> to accept or <#ffff00>/tpdeny<#ffaa00> to deny.\nRequest will expire in <#ff5555>30<#ffaa00> seconds.");
                    } else {
                        utils.sendColorText(audience, "<#ffaa00>No player player named <#aa0000>" + args[0]+"<#ffaa00> was found.");
                    }
                } else {
                    utils.sendColorText(audience, "<#ffaa00>Usage: <#ffffff>/tpa <#ffff00><player>");
                }
                break;
            case "tpaccept": {
                PlayerData target = plugin.players.get(player.getUniqueId());
                request request = target.acceptLastRequest();
                if (request != null) {
                    if (request.type.equals("tpa")) {
                        request.host.teleport(player);
                    } else if (request.type.equals("tpahere")) {
                        player.teleport(request.host);
                    }
                    utils.sendColorText(audience, "<#ffaa00>Request accepted.");
                } else {
                    utils.sendColorText(audience, "<#ffaa00>No requests to accept.");
                }
            }    break;
            case "tpdeny": {
                PlayerData host = plugin.players.get(player.getUniqueId());
                boolean success = host.denyLastRequest();
                if (success) {
                    utils.sendColorText(audience, "<#ffaa00>Request denied.");
                } else {
                    utils.sendColorText(audience, "<#ffaa00>No requests to deny.");
                }
            }    break;
            case "setwarp": {
                if (args.length == 1) {
                    plugin.warps.set(args[0]+ ".x", player.getLocation().getX());
                    plugin.warps.set(args[0]+ ".y", player.getLocation().getY());
                    plugin.warps.set(args[0]+ ".z", player.getLocation().getZ());
                    plugin.warps.set(args[0]+ ".world", player.getWorld().getUID().toString());
                    try {
                        plugin.warps.save();
                    } catch (Exception e) {
                        utils.sendColorText(audience, "<#ffaa00>Failed to save warps.yml.");
                    }
                    utils.sendColorText(audience, "<#ffaa00>Warp <#ffff00>" + args[0] + "<#ffaa00> set.");
                } else {
                    utils.sendColorText(audience, "<#ffaa00>Usage: <#ffffff>/setwarp <#ffff00><name>");
                }
            }   break;
            case "warps": {
                    utils.sendColorText(audience, "<#ffaa00>Warps:");
                    for (String key : plugin.warps.getRoutesAsStrings(false)) {
                        utils.sendColorText(audience, "<#ffaa00> - <#ffff00>" + key);
                    }
            }   break;
            case "warp": {
                if (args.length == 1) {
                    if (plugin.warps.get(args[0]) != null) {
                        World world = plugin.getServer().getWorld(UUID.fromString(plugin.warps.getString(args[0]+ ".world")));
                        if (world != null) {
                            double X = plugin.warps.getDouble(args[0]+ ".x");
                            double Y = plugin.warps.getDouble(args[0]+ ".y");
                            double Z = plugin.warps.getDouble(args[0]+ ".z");
                            player.teleport(new Location(world, X, Y, Z));
                            utils.sendColorText(audience, "<#ffaa00>Teleported to <#ffff00>" + args[0]);
                        } else {
                            utils.sendColorText(audience, "<#ffaa00>World no longer exists for warp <#ffff00>" + args[0]);
                        }
                    } else {
                        utils.sendColorText(audience, "<#ffaa00>Warp <#ffff00>" + args[0] + "<#ffaa00> not found.");
                    }
                } else {
                    utils.sendColorText(audience, "<#ffaa00>Usage: <#ffffff>/warp <#ffff00><name>\n<#ffaa00>Warps:");
                    for (String key : plugin.warps.getRoutesAsStrings(false)) {
                        utils.sendColorText(audience, "<#ffaa00> - <#ffff00>" + key);
                    }
                }
            }   break;
            case "delwarp": {
                if (args.length == 1) {
                    if (plugin.warps.get(args[0]) != null) {
                        plugin.warps.remove(args[0]);
                        try {
                            plugin.warps.save();
                        } catch (Exception e) {
                            utils.sendColorText(audience, "<#ffaa00>Failed to save warps.yml.");
                        }
                        utils.sendColorText(audience, "<#ffaa00>Warp <#ffff00>" + args[0] + "<#ffaa00> deleted.");
                    } else {
                        utils.sendColorText(audience, "<#ffaa00>Warp <#ffff00>" + args[0] + "<#ffaa00> not found.");
                    }
                } else {
                    utils.sendColorText(audience, "<#ffaa00>Usage: <#ffffff>/delwarp <#ffff00><name>");
                }
            }   break;
            default:
                break;
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