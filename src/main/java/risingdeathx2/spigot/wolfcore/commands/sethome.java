package risingdeathx2.spigot.wolfcore.commands;

import risingdeathx2.spigot.wolfcore.core;
import risingdeathx2.spigot.wolfcore.utils;
import risingdeathx2.spigot.wolfcore.classes.Argument;
import risingdeathx2.spigot.wolfcore.classes.ArgumentType;
import risingdeathx2.spigot.wolfcore.classes.Command;
import risingdeathx2.spigot.wolfcore.classes.CoreCommandExecutor;
import risingdeathx2.spigot.wolfcore.classes.Home;
import risingdeathx2.spigot.wolfcore.classes.PlayerData;
import risingdeathx2.spigot.wolfcore.utilities.PermissionHandler;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.kyori.adventure.audience.Audience;
import net.luckperms.api.model.user.User;

import java.util.ArrayList;

public class sethome implements CoreCommandExecutor {
    @Override
    public Command getCommand() {
        return new Command("sethome","wolfcore.sethome", new ArrayList<>() {{
            add(new Argument("home", ArgumentType.STRING, true));
        }});
    }

    core core;
    public sethome(core core) {
        this.core = core;
    }
    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        Audience audience = core.adventure().sender(sender);
        User user = core.lp.getUserManager().getUser(sender.getName());
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
            if (playerData.homes.size() > PermissionHandler.getNumberValue("wolfcore.sethome", user).intValue()) {
                utils.sendColorText(audience, core.getMessage("home.limit", List.of(PermissionHandler.getNumberValue("wolfcore.sethome", user).toString())));
                return true;
            }
            playerData.homes.put(home, new Home(home, ((Player) sender).getLocation()));
            utils.sendColorText(audience, core.getMessage("home.set", List.of(home)));
        } else {
            utils.sendColorText(audience, core.getMessage("generic.invaliddata"));
        }
        return true;
    }

}
