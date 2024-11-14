package com.wolfco.main.classes.customArgs;

import java.util.List;
import java.util.UUID;

import org.bukkit.command.CommandSender;

import com.wolfco.common.classes.ArgumentInterface;
import com.wolfco.common.classes.ArgumentType;
import com.wolfco.common.classes.CorePlugin;
import com.wolfco.main.Core;
import com.wolfco.main.classes.Warp;

import dev.dejvokep.boostedyaml.YamlDocument;
 
public class WarpArgument implements ArgumentInterface {
    private final boolean required = true;

    public WarpArgument(boolean required) { 
    }

    @Override
    public ArgumentType getType() {
        return ArgumentType.CUSTOM;
    }

    @Override
    public Boolean isRequired() {
        return required;
    }

    @Override
    public String getName() {
        return "WARP";
    }

    @Override
    public List<String> getOptions(CorePlugin core, CommandSender sender,
            org.bukkit.command.Command bukkitCommand, String[] args) {
        Core plugin = (Core) core;

        return plugin.warps.getRoutesAsStrings(false).stream().toList();
    }

    @Override
    public Warp getValue(CorePlugin core, CommandSender sender, org.bukkit.command.Command bukkitCommand,
            String searchValue) {
        Core plugin = (Core) core;
        YamlDocument warps = plugin.warps;
        
        if (!warps.contains(searchValue)) return null;

        Warp warp = new Warp();

        warp.name = searchValue;
        warp.world = UUID.fromString(warps.getString(searchValue + ".world"));
        warp.x = warps.getDouble(searchValue + ".x");
        warp.y = warps.getDouble(searchValue + ".y");
        warp.z = warps.getDouble(searchValue + ".z");
        return warp;
    }
}
