package com.wolfco.skyblock.commands;

import com.wolfco.skyblock.main;
import com.wolfco.skyblock.classes.Argument;
import com.wolfco.skyblock.classes.ArgumentType;
import com.wolfco.skyblock.classes.Command;
import com.wolfco.skyblock.classes.CoreCommandExecutor;
import java.util.ArrayList;

import org.bukkit.command.CommandSender;

public class baseCommand implements CoreCommandExecutor {
    public Command getCommand() {
        return new Command("placeholder","placeholder", new ArrayList<Argument>() {
            {
                add(new Argument("placeholder", ArgumentType.GAMEMODE, false));
                add(new Argument("placeholder", ArgumentType.PLAYER, true));
            }
        });
    }

    public main main;

    public baseCommand(main main) {
        this.main = main;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        return false;
    }
}
