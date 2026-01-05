package com.wolfco.main.commands.arguments;

import java.util.List;

import com.wolfco.common.classes.ArgumentInterface;
import com.wolfco.main.Core;
import com.wolfco.main.warps.Warp;
import com.wolfco.main.warps.WarpManager;

import io.papermc.paper.command.brigadier.CommandSourceStack;
 
public class WarpArgument implements ArgumentInterface {
    private final WarpManager warpManager = Core.get().getScope().get(WarpManager.class);

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
        return warpManager.getWarps();
    }

    @Override
    public Warp getValue(CommandSourceStack commandStack,
            String searchValue) {
        return warpManager.getWarp(searchValue);
    }
}
