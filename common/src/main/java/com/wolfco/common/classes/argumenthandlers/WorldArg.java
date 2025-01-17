package com.wolfco.common.classes.argumenthandlers;

import java.util.List;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.wolfco.common.classes.ArgumentInterface;
import com.wolfco.common.classes.CorePlugin;

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
    public List<String> getOptions(CorePlugin core, CommandSender sender, Command bukkitCommand, String[] args) {
        return core.getServer().getWorlds().stream().map(w -> w.getName()).toList();
    }

    @Override
    public World getValue(CorePlugin core, CommandSender sender, Command bukkitCommand, String searchValue) {
        World world = core.getServer().getWorld(searchValue);

        if (world instanceof World) {
            return core.getServer().getWorld(searchValue);
        }
        
        throw error("Argument %s requires a valid world. Possible values are: [%s]", name,
                String.join(", ", core.getServer().getWorlds().stream().map(w -> w.getName()).toList()));
    }

}
