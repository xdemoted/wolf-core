package risingdeathx2.spigot.wolfcore.commands;

import risingdeathx2.spigot.wolfcore.core;
import risingdeathx2.spigot.wolfcore.utils;
import risingdeathx2.spigot.wolfcore.classes.Argument;
import risingdeathx2.spigot.wolfcore.classes.ArgumentType;
import risingdeathx2.spigot.wolfcore.classes.Command;
import risingdeathx2.spigot.wolfcore.classes.CoreCommandExecutor;
import risingdeathx2.spigot.wolfcore.classes.Home;
import risingdeathx2.spigot.wolfcore.classes.PlayerData;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.kyori.adventure.audience.Audience;

import java.util.ArrayList;

public class sethome implements CoreCommandExecutor {
    @Override
    public Command getCommand() {
        return new Command("sethome", new ArrayList<>() {{
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
            playerData.homes.put(home, new Home(home, ((Player) sender).getLocation()));
            utils.sendColorText(audience, core.getMessage("home.set", List.of(home)));
        } else {
            utils.sendColorText(audience, core.getMessage("generic.invaliddata"));
        }
        return true;
    }

}
