package com.wolfco.common.commands.arguments;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;

import com.wolfco.common.classes.ArgumentInterface;

import io.papermc.paper.command.brigadier.CommandSourceStack;

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
    public List<String> getOptions(CommandSourceStack commandStack, String[] args) {
        List<String> enchantNames = new java.util.ArrayList<>();
        for (Enchantment enchantment : Registry.ENCHANTMENT) {
            NamespacedKey key = enchantment.getKey();
            enchantNames.add(key.getKey());
        }
        return enchantNames;
    }

    @Override
    public Enchantment getValue(CommandSourceStack commandStack, String searchValue) {
        NamespacedKey key = NamespacedKey.fromString(searchValue);

        if (key == null) {
            throw error("Argument %s requires a valid enchant. Possible values are: [%s]", name,
                    Registry.ENCHANTMENT.stream()
                            .map(e -> {
                                return e.getKey().getKey();
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
                            return e.getKey().getKey();
                        })
                        .collect(Collectors.joining(", ")));
    }

}
