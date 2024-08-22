package risingdeathx2.spigot.wolfcore.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import risingdeathx2.spigot.wolfcore.core;
import risingdeathx2.spigot.wolfcore.utils;
import risingdeathx2.spigot.wolfcore.classes.Argument;
import risingdeathx2.spigot.wolfcore.classes.ArgumentType;
import risingdeathx2.spigot.wolfcore.classes.Command;
import risingdeathx2.spigot.wolfcore.classes.CoreCommandExecutor;

public class teleportall implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        return new Command("teleportall","wolfcore.tpall", new ArrayList<>() {{
            add(new Argument("player", ArgumentType.PLAYER, true));
        }});
    }
    core core;
    public teleportall(core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        if (args.length == 0 && sender instanceof Player) {
            core.getServer().getOnlinePlayers().forEach(player -> {
                player.teleport(((org.bukkit.entity.Player) sender));
            });
            utils.sendColorText(core.adventure().sender(sender), core.getMessage("teleportall.success",List.of("you")));
            return true;
        } else if (args.length == 1) {
            Player player = getCommand().options.get(0).getExclusivePlayer(core, args[0]);
            if (player == null) {
                utils.sendColorText(core.adventure().sender(sender), core.getMessage("generic.playernotfound"));
                return false;
            }
            core.getServer().getOnlinePlayers().forEach(p -> {
                p.teleport(player);
            });
            utils.sendColorText(core.adventure().sender(sender), core.getMessage("teleportall.success",List.of(player.getName())));
            return true;
        }
        return false;
    }
    
}
