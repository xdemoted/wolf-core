package com.wolfco.main.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.main.core;
import com.wolfco.common.utils;
import com.wolfco.common.classes.Argument;
import com.wolfco.common.classes.ArgumentType;
import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;

public class teleporthere implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        return new Command("teleporthere","wolfcore.tphere", new ArrayList<>() {{
            add(new Argument("player", ArgumentType.PLAYER, false));
        }});
    }

    @Override
    public core fetchCore() {
        return core;
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
