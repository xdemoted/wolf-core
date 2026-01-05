package com.wolfco.main.commands;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.common.commands.arguments.EnchantArg;
import com.wolfco.common.commands.arguments.NumberArg;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import jakarta.inject.Singleton;

@Singleton
public class Enchant implements CoreCommand {
    @Override
    public Command getCommand() {
        Command command = new Command().setName("enchant");
        command.setAccessType(AccessType.PLAYER);
        command.addArguments(
                new EnchantArg(true).setName("ENCHANTMENT"),
                new NumberArg(true, 0).setConstraints(0, 255)
        );

        return command;
    }
    
    @Override
    public boolean onCommand(CommandSourceStack commandStack, String[] args, Object[] argumentValues) {
        Enchantment enchantment = (Enchantment) argumentValues[0];
        Player player = (Player) commandStack.getSender();

        player.getInventory().getItemInMainHand().addUnsafeEnchantment(enchantment, (int) Math.round((Double) argumentValues[1]));
        return true;
    }
}
