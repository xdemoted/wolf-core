package com.wolfco.main.commands;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.common.commands.arguments.NumberArg;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import jakarta.inject.Singleton;

@Singleton
public class Reach implements CoreCommand {
    @Override
    public Command getCommand() {
        Command command = new Command().setName("reach");
        command.setAccessType(AccessType.PLAYER);
        command.addArguments(new NumberArg(true, 0).setConstraints(0, 20).setName("DISTANCE"));

        return command;
    }
    
    @Override
    public boolean onCommand(CommandSourceStack commandStack, String[] args, Object[] argumentValues) {
        CommandSender sender = commandStack.getSender();
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

        getMessageUtility().sendMessage(sender, "Reach set to " + distance);
        
        return true;
    }

}
