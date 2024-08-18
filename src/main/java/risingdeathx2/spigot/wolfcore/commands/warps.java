package risingdeathx2.spigot.wolfcore.commands;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import net.kyori.adventure.audience.Audience;
import risingdeathx2.spigot.wolfcore.core;
import risingdeathx2.spigot.wolfcore.utils;

public class warps {
    public warps(core core, Player player, String alias, String[] args, Audience audience) {
        switch (alias) {
            case "warp":
                if (args.length == 1) {
                    if (core.warps.get(args[0]) != null) {
                        World world = core.getServer().getWorld(UUID.fromString(core.warps.getString(args[0]+ ".world")));
                        if (world != null) {
                            double X = core.warps.getDouble(args[0]+ ".x");
                            double Y = core.warps.getDouble(args[0]+ ".y");
                            double Z = core.warps.getDouble(args[0]+ ".z");
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
                    for (String key : core.warps.getRoutesAsStrings(false)) {
                        utils.sendColorText(audience, "<#ffaa00> - <#ffff00>" + key);
                    }
                }
                break;
            case "warps":
                utils.sendColorText(audience, "<#ffaa00>Warps:");
                for (String key : core.warps.getRoutesAsStrings(false)) {
                    utils.sendColorText(audience, "<#ffaa00> - <#ffff00>" + key);
                }
                break;
            case "setwarp":
                if (args.length == 1) {
                    core.warps.set(args[0]+ ".x", player.getLocation().getX());
                    core.warps.set(args[0]+ ".y", player.getLocation().getY());
                    core.warps.set(args[0]+ ".z", player.getLocation().getZ());
                    core.warps.set(args[0]+ ".world", player.getWorld().getUID().toString());
                    try {
                        core.warps.save();
                    } catch (Exception e) {
                        utils.sendColorText(audience, "<#ffaa00>Failed to save warps.yml.");
                    }
                    utils.sendColorText(audience, "<#ffaa00>Warp <#ffff00>" + args[0] + "<#ffaa00> set.");
                } else {
                    utils.sendColorText(audience, "<#ffaa00>Usage: <#ffffff>/setwarp <#ffff00><name>");
                }
                break;
            case "delwarp":
                if (args.length == 1) {
                    if (core.warps.get(args[0]) != null) {
                        core.warps.remove(args[0]);
                        try {
                            core.warps.save();
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
                break;
            case "warpinfo": {
                if (args.length == 1) {
                    if (core.warps.get(args[0]) != null) {
                        utils.sendColorText(audience, "<#ffaa00>Warp <#ffff00>" + args[0] + "<#ffaa00>:");
                        utils.sendColorText(audience, "<#ffaa00> - <#ffff00>World: <#ffaa00>" + core.warps.getString(args[0]+ ".world"));
                        utils.sendColorText(audience, "<#ffaa00> - <#ffff00>X: <#ffaa00>" + core.warps.getDouble(args[0]+ ".x"));
                        utils.sendColorText(audience, "<#ffaa00> - <#ffff00>Y: <#ffaa00>" + core.warps.getDouble(args[0]+ ".y"));
                        utils.sendColorText(audience, "<#ffaa00> - <#ffff00>Z: <#ffaa00>" + core.warps.getDouble(args[0]+ ".z"));
                    } else {
                        utils.sendColorText(audience, "<#ffaa00>Warp <#ffff00>" + args[0] + "<#ffaa00> not found.");
                    }
                } else {
                    utils.sendColorText(audience, "<#ffaa00>Usage: <#ffffff>/warpinfo <#ffff00><name>");
                }
                break;
            }
            default:
                break;
        }
    }
}
