package com.wolfco.main.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.argumenthandlers.EnchantArg;
import com.wolfco.common.classes.argumenthandlers.NumberArg;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.main.Core;

public class Enchant implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("enchant");
        command.setAccessType(AccessType.PLAYER);
        command.addArguments(
                new EnchantArg(true).setName("ENCHANTMENT"),
                new NumberArg(true, 0).setConstraints(0, 255)
        );

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public Enchant(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args,
            Object[] argumentValues) {
        Enchantment enchantment = (Enchantment) argumentValues[0];
        Player player = (Player) sender;

        player.getInventory().getItemInMainHand().addUnsafeEnchantment(enchantment, (int) Math.round((Double) argumentValues[1]));
        return true;
    }
}
