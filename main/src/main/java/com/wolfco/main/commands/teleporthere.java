spackage com.wolfco.main.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.main.Core;
import com.wolfco.common.Utilities;
import com.wolfco.common.classes.Argument;
import com.wolfco.common.classes.ArgumentType;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;

public class teleporthere implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("teleporthere");
        command.setDescription("Teleport a player to you.");
        command.setNode("wolfcore.teleporthere");
        command.setArguments(new ArrayList<>() {{
            add(new Argument(ArgumentType.OTHERPLAYER, true));
        }});
        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }
    
    Core core;
    public teleporthere(Core core) {
        this.core = core;
    }
    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        if (sender instanceof Player) {
            Player player = getCommand().options.get(0).getExclusivePlayer(core, args[0]);
            if (player == null) {
                Utilities.sendColorText(core.getAdventure().sender(sender), core.getMessage("generic.playernotfound"));
                return false;
            }
            player.teleport(((Player) sender));
            Utilities.sendColorText(core.getAdventure().sender(sender), core.getMessage("teleporthere.success",List.of(player.getName())));
            return true;
        } else {
            Utilities.sendColorText(core.getAdventure().sender(sender), core.getMessage("generic.noconsole"));
            return false; 
        }
    } 
}
