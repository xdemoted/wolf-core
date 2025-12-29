package com.wolfco.main.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.common.commands.arguments.PlayerArg;
import com.wolfco.main.Core;
import com.wolfco.main.utility.FontUtil;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class InventorySee implements CoreCommand {
    @Override
    public Command getCommand() {
        Command command = new Command().setName("inventorysee");
        command.setAccessType(AccessType.PLAYER);
        command.addArguments(new PlayerArg(true).includeSender(false));

        return command;
    }

    @Inject
    Core core;
    
    @Override
    public boolean onCommand(CommandSourceStack commandStack, String[] args, Object[] argumentValues) {
        CommandSender sender = commandStack.getSender();
        Player target = (Player) argumentValues[0];

        PlayerInventory targetInventory = target.getInventory();
        Inventory chestInventory = core.getServer().createInventory(null, 54, FontUtil.getPlayerTag(target) + "'s Inventory");
        chestInventory.setContents(targetInventory.getContents());

        ((Player) sender).openInventory(chestInventory);
        return true;
    }

}
