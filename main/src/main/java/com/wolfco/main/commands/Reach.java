package com.wolfco.main.commands;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.argumenthandlers.NumberArg;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.main.Core;

public class Reach implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("reach");
        command.setAccessType(AccessType.PLAYER);
        command.addArguments(new NumberArg(true, 0).setConstraints(0, 20).setName("DISTANCE"));

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public Reach(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        Double distance = (Double) argumentValues[0];
        Player player = (Player) sender;

        AttributeInstance attInstance = player.getAttribute(Attribute.BLOCK_INTERACTION_RANGE);
        AttributeInstance attInstance2 = player.getAttribute(Attribute.ENTITY_INTERACTION_RANGE);

        if (attInstance != null) {
            attInstance.setBaseValue(distance);
        }

        if (attInstance2 != null) {
            attInstance2.setBaseValue(distance);
        }

        core.sendMessage(sender, "Reach set to " + distance);
        
        return true;
    }

}
