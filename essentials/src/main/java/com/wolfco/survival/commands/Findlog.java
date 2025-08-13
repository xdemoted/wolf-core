package com.wolfco.survival.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.argumenthandlers.NumberArg;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.survival.Core;
import com.wolfco.survival.utils;

public class Findlog implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        Command command = new Command("findlog");
        command.setAccessType(AccessType.PLAYER);
        command.addArguments(
                new NumberArg(true, 0).setName("x"),
                new NumberArg(true, 0).setName("y"),
                new NumberArg(true, 0).setName("z"));
        command.setAccessType(AccessType.ALL);
        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public Findlog(Core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args,
            Object[] argumentValues) {
        double x = (double) argumentValues[0];
        double y = (double) argumentValues[1];
        double z = (double) argumentValues[2];
        sender.sendMessage(sender.getClass().getName());

        if (sender instanceof Entity player) {
            Block block = player.getWorld().getBlockAt((int) x, (int) y, (int) z);

            if (utils.leaves.contains(block.getType())) {
                
                return true;
            } else {
                sender.sendMessage("No logs or leaves found at " + block.getLocation().toString());
            }
        }

        return false;
    }

    public static boolean isBlockNearLogDebug(Block block, Location... excludes) {
        Map<Location, Material> originalBlocks = new HashMap<>();
        List<Location> excludedBlocks = new ArrayList<>(Arrays.asList(excludes));
        Location loc = block.getLocation();
        var pos1 = loc.clone().subtract(6,6,6);
        var pos2 = loc.clone().add(6,6,6);

        boolean logFound = false;
        List<Block> blocks = new ArrayList<>();

        List<Block> nextBlocks = new ArrayList<>();
        nextBlocks.add(block);

        Bukkit.getScheduler().runTaskTimer(Core.get(), (task) -> {
            iterate(blocks, nextBlocks, excludedBlocks, pos1, pos2, logFound, originalBlocks);
            
            if (nextBlocks.isEmpty() || logFound) {
                task.cancel(); // Stop the task if no more blocks to check or log found

                Bukkit.getScheduler().runTaskLater(Core.get(), () -> {
                    for (Map.Entry<Location, Material> entry : originalBlocks.entrySet()) {
                        Block blockToRestore = entry.getKey().getBlock();
                        blockToRestore.setType(entry.getValue());
                    }
                }, 100L); // Restore blocks after 1 second (20 ticks)
            }
        }, 0L, 20L); // Schedule the task to run every second

        return logFound;
    }

    public static void iterate(List<Block> blocks, List<Block> nextBlocks, List<Location> excludedBlocks, Location pos1, Location pos2, boolean logFound, Map<Location, Material> originalBlocks) {
        blocks = new ArrayList<>(nextBlocks);
        nextBlocks = new ArrayList<>();

        for (Block hostBlock : blocks) {
            List<Block> adjacentBlocks = utils.getAdjacentBlocks(hostBlock);

            for (Block adjacentBlock : adjacentBlocks) {
                if (nextBlocks.contains(adjacentBlock) || blocks.contains(adjacentBlock) || excludedBlocks.contains(adjacentBlock.getLocation())) continue;

                if (utils.isBlockInRegion(adjacentBlock.getLocation(), pos1, pos2)) {
                    if (utils.logsThatBurn.contains(adjacentBlock.getType())) {
                        originalBlocks.put(adjacentBlock.getLocation(), adjacentBlock.getType());
                        adjacentBlock.setType(Material.BROWN_CONCRETE);
                        logFound = true;
                        break;
                    } else if (utils.leaves.contains(adjacentBlock.getType())) {
                        originalBlocks.put(adjacentBlock.getLocation(), adjacentBlock.getType());
                        adjacentBlock.setType(Material.RED_CONCRETE);
                        nextBlocks.add(adjacentBlock);
                    }
                }
            }

            if (logFound) break;
            hostBlock.setType(Material.BLACK_STAINED_GLASS);
            excludedBlocks.add(hostBlock.getLocation());
        }
    }
}
