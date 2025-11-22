package com.wolfco.common.classes.argumenthandlers;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;

import com.wolfco.common.classes.ArgumentInterface;
import com.wolfco.common.classes.CorePlugin;

public class EnchantArg implements ArgumentInterface {

    final boolean required;
    String name = "ENCHANTMENT";

    public EnchantArg(boolean required) {
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
        List<String> enchantNames = new java.util.ArrayList<>();
        for (Enchantment enchantment : Registry.ENCHANTMENT) {
            NamespacedKey key = enchantment.getKeyOrNull();
            if (key != null) {
                enchantNames.add(key.getKey());
            }
        }
        return enchantNames;
    }

    @Override
    public Enchantment getValue(CorePlugin core, CommandSender sender, Command bukkitCommand, String searchValue) {
        NamespacedKey key = NamespacedKey.fromString(searchValue);

        if (key == null) {
            throw error("Argument %s requires a valid enchant. Possible values are: [%s]", name,
                    Registry.ENCHANTMENT.stream()
                            .map(e -> {
                                NamespacedKey k = e.getKeyOrNull();
                                return k != null ? k.getKey() : "";
                            })
                            .collect(Collectors.joining(", ")));
        }

        Enchantment enchant = Registry.ENCHANTMENT.get(key);

        if (enchant instanceof Enchantment) {
            return enchant;
        }

        throw error("Argument %s requires a valid enchant. Possible values are: [%s]", name,
                Registry.ENCHANTMENT.stream()
                        .map(e -> {
                            NamespacedKey k = e.getKeyOrNull();
                            return k != null ? k.getKey() : "";
                        })
                        .collect(Collectors.joining(", ")));
    }

}
