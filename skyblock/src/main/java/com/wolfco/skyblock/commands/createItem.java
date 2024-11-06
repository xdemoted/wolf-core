package com.wolfco.skyblock.commands;

import com.wolfco.skyblock.main;
import com.wolfco.skyblock.classes.Argument;
import com.wolfco.skyblock.classes.ArgumentType;
import com.wolfco.skyblock.classes.Command;
import com.wolfco.skyblock.classes.CoreCommandExecutor;
import java.util.ArrayList;

import org.bukkit.command.CommandSender;

public class createItem implements CoreCommandExecutor {
    public Command getCommand() {
        return new Command("gamemode","wolfmain.gamemode", new ArrayList<Argument>() {
            {
                add(new Argument("gamemode", ArgumentType.GAMEMODE, false));
                add(new Argument("player", ArgumentType.PLAYER, true));
            }
        });
    }

    public main main;

    public createItem(main main) {
        this.main = main;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        
        return true;
    }
}
