package risingdeathx2.spigot.wolfcore.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.kyori.adventure.audience.Audience;
import risingdeathx2.spigot.wolfcore.core;
import risingdeathx2.spigot.wolfcore.utils;
import risingdeathx2.spigot.wolfcore.classes.Argument;
import risingdeathx2.spigot.wolfcore.classes.ArgumentType;
import risingdeathx2.spigot.wolfcore.classes.Command;
import risingdeathx2.spigot.wolfcore.classes.CoreCommandExecutor;
import risingdeathx2.spigot.wolfcore.classes.Home;
import risingdeathx2.spigot.wolfcore.classes.PlayerData;

public class home implements CoreCommandExecutor {
    @Override
    public Command getCommand() {
        return new Command("home", new ArrayList<>() {{
            add(new Argument("home", ArgumentType.HOME, true));
        }});
    }

    core core;
    public home(core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        Audience audience = core.adventure().sender(sender);
        if (!(sender instanceof Player)) {
            utils.sendColorText(audience, core.getMessage("generic.noconsole"));
            return true;
        }
        String home;
        if (args.length == 0) {
            home = "home";
        } else {
            home = args[0];
        }
        if (!home.matches("^[a-zA-Z0-9]+$")) {
            utils.sendColorText(audience, core.getMessage("generic.alphanumeric",List.of(getCommand().options.get(0).name)));
            return true;
        }
        PlayerData playerData = core.playerManager.getPlayerData((Player) sender);
        if (playerData != null) {
            Home result = getCommand().options.get(0).getHome(core, home, playerData);
            if (result == null) {
                utils.sendColorText(audience, core.getMessage("home.notfound", List.of(home)));
                return true;
            }
            World world = core.getServer().getWorld(result.world);
            if (world == null) {
                utils.sendColorText(audience, core.getMessage("home.worldinvalid", List.of(result.world.toString(), home)));
                return true;
            }
            Location location = new Location(world, result.x, result.y, result.z, result.yaw, result.pitch);
            ((Player) sender).teleport(location);
            utils.sendColorText(audience, core.getMessage("home.teleported", List.of(home)));
        }
        return true;
    }
}
