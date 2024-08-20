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

public class teleporthere implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        return new Command("teleporthere", new ArrayList<>() {{
            add(new Argument("player", ArgumentType.PLAYER, false));
        }});
    }
    core core;
    public teleporthere(core core) {
        this.core = core;
    }
    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        if (sender instanceof Player) {
            Player player = getCommand().options.get(0).getExclusivePlayer(core, args[0]);
            if (player == null) {
                utils.sendColorText(core.adventure().sender(sender), core.getMessage("generic.playernotfound"));
                return false;
            }
            player.teleport(((Player) sender));
            utils.sendColorText(core.adventure().sender(sender), core.getMessage("teleporthere.success",List.of(player.getName())));
            return true;
        } else {
            utils.sendColorText(core.adventure().sender(sender), core.getMessage("generic.noconsole"));
            return false; 
        }
    } 
}
