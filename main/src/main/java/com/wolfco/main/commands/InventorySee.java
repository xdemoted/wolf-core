package com.wolfco.main.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.argumenthandlers.PlayerArg;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.main.Core;

public class InventorySee implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("inventorysee");
        command.setAccessType(AccessType.PLAYER);
        command.addArguments(new PlayerArg(true).includeSender(false));

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public InventorySee(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        Player target = (Player) argumentValues[0];

        PlayerInventory targetInventory = target.getInventory();
        Inventory chestInventory = core.getServer().createInventory(null, 54, target.getName() + "'s Inventory");
        chestInventory.setContents(targetInventory.getContents());

        

        ((Player) sender).openInventory(chestInventory);
        return true;
    }

}
