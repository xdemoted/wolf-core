package risingdeathx2.spigot.wolfcore.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.kyori.adventure.audience.Audience;
import risingdeathx2.spigot.wolfcore.classes.Command;
import risingdeathx2.spigot.wolfcore.classes.CoreCommandExecutor;
import risingdeathx2.spigot.wolfcore.classes.Home;
import risingdeathx2.spigot.wolfcore.classes.PlayerData;
import risingdeathx2.spigot.wolfcore.core;
import risingdeathx2.spigot.wolfcore.utils;
import risingdeathx2.spigot.wolfcore.classes.Argument;
import risingdeathx2.spigot.wolfcore.classes.ArgumentType;
import java.util.ArrayList;
import java.util.List;

public class delhome implements CoreCommandExecutor {
    @Override
    public Command getCommand() {
        return new Command("delhome", new ArrayList<>() {{
            add(new Argument("home", ArgumentType.STRING, true));
        }});
    }

    core core;
    public delhome(core core) {
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
            Home result = playerData.homes.remove(home);
            if (result == null) {
                utils.sendColorText(audience, core.getMessage("home.notfound", List.of(home)));
                return true;
            }
            utils.sendColorText(audience, core.getMessage("home.deleted", List.of(home)));
        }
        return true;
    }
}
