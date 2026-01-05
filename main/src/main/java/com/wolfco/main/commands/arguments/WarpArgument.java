package com.wolfco.main.commands.arguments;

import java.util.List;
import java.util.UUID;

import com.wolfco.common.classes.ArgumentInterface;
import com.wolfco.main.Core;
import com.wolfco.main.warps.Warp;

import dev.dejvokep.boostedyaml.YamlDocument;
import io.papermc.paper.command.brigadier.CommandSourceStack;
 
public class WarpArgument implements ArgumentInterface {
    private boolean required = true;
    private String name = "WARP";

    public WarpArgument(boolean required) { 
        this.required = required;
    }
    
    @Override
    public Boolean isRequired() {
        return required;
    }

    @Override
    public ArgumentInterface setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getOptions(CommandSourceStack commandStack, String[] args) {
        Core plugin = Core.get();

        return plugin.getWarps().getRoutesAsStrings(false).stream().toList();
    }

    @Override
    public Warp getValue(CommandSourceStack commandStack,
            String searchValue) {
        Core plugin = Core.get();
        YamlDocument warps = plugin.getWarps();
        
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
