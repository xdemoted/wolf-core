package com.wolfco.common.commands.arguments;

import java.util.List;

import org.bukkit.World;

import com.wolfco.common.classes.ArgumentInterface;
import com.wolfco.common.classes.CorePlugin;

import io.papermc.paper.command.brigadier.CommandSourceStack;

public class WorldArg implements ArgumentInterface {

    final boolean required;
    String name = "BOOLEAN";

    public WorldArg(boolean required) {
        this.required = required;
    }

    @Override
    public Boolean isRequired() {
        return required;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ArgumentInterface setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public List<String> getOptions(CommandSourceStack commandStack, String[] args) {
        CorePlugin core = CorePlugin.get();
        return core.getServer().getWorlds().stream().map(w -> w.getName()).toList();
    }

    @Override
    public World getValue(CommandSourceStack commandStack, String searchValue) {
        CorePlugin core = CorePlugin.get();
        
        World world = core.getServer().getWorld(searchValue);

        if (world instanceof World) {
            return core.getServer().getWorld(searchValue);
        }
        
        throw error("Argument %s requires a valid world. Possible values are: [%s]", name,
                String.join(", ", core.getServer().getWorlds().stream().map(w -> w.getName()).toList()));
    }

}
