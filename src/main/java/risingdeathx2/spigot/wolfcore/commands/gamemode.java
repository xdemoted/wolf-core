package risingdeathx2.spigot.wolfcore.commands;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.kyori.adventure.audience.Audience;

import java.util.ArrayList;
import java.util.List;

import risingdeathx2.spigot.wolfcore.core;
import risingdeathx2.spigot.wolfcore.utils;
import risingdeathx2.spigot.wolfcore.classes.Argument;
import risingdeathx2.spigot.wolfcore.classes.ArgumentType;
import risingdeathx2.spigot.wolfcore.classes.Command;
import risingdeathx2.spigot.wolfcore.classes.CoreCommandExecutor;

public class gamemode implements CoreCommandExecutor {
    public Command getCommand() {
        return new Command("gamemode", new ArrayList<Argument>() {
            {
                add(new Argument("gamemode", ArgumentType.GAMEMODE, false));
                add(new Argument("player", ArgumentType.PLAYER, true));
            }
        });
    }

    core core;

    public gamemode(core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        Boolean console = false;
        Audience senderAudience = core.adventure().sender(sender);
        if (!(sender instanceof Player)) {
            console = true;
        }
        if (console && args.length == 1) {
            utils.sendColorText(senderAudience, core.getMessage("generic.consoleargs", List.of("2")));
            return false;
        }
        GameMode mode = (GameMode) this.getCommand().options.get(0).getGamemode(core, args[0]);
        if (mode == null) {
            utils.sendColorText(senderAudience, core.getMessage("gamemode.invalid"));
            return false;
        }
        if (args.length == 1) {
            if (console) {
                utils.sendColorText(senderAudience, core.getMessage("generic.consoleargs", List.of("2")));
                return false;
            }
            ((Player) sender).setGameMode(mode);
            utils.sendColorText(senderAudience, core.getMessage("gamemode.selfsuccess", List.of(mode.toString())));
            return true;
        } else if (args.length == 2) {
            List<Player> target = this.getCommand().options.get(1).getPlayer(core, args[1]);
            if (target == null) {
                utils.sendColorText(senderAudience, core.getMessage("generic.playernotfound"));
                return false;
            }
            if (target.size() == 1) {
                target.get(0).setGameMode(mode);
                utils.sendColorText(senderAudience,
                        core.getMessage("gamemode.othersuccess", List.of(target.get(0).getName(), mode.toString())));
            } else {
                for (Player p : target) {
                    p.setGameMode(mode);
                }
                utils.sendColorText(senderAudience, core.getMessage("gamemode.multisuccess",
                        List.of(String.valueOf(target.size()), mode.toString())));
            }
            return true;
        }
        return true;
    }
}
