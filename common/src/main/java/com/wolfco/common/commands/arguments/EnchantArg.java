package com.wolfco.common.commands.arguments;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

import com.wolfco.common.classes.ArgumentInterface;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;

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
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).stream()
                .map(e -> e.getKey().getKey())
                .collect(Collectors.toList());
    }

    @Override
    public Enchantment getValue(CommandSourceStack commandStack, String searchValue) {
        NamespacedKey key = NamespacedKey.fromString(searchValue);

        if (key == null) {
            throw error("Argument %s requires a valid enchant. Possible values are: [%s]", name,
                    RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).stream()
                            .map(e -> e.getKey().getKey())
                            .collect(Collectors.joining(", ")));
        }

        Enchantment enchant = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(key);

        if (enchant != null) {
            return enchant;
        }

        throw error("Argument %s requires a valid enchant. Possible values are: [%s]", name,
                RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).stream()
                        .map(e -> e.getKey().getKey())
                        .collect(Collectors.joining(", ")));
    }

    private String node = null;

    @Override
    public ArgumentInterface setNode(String node) {
        this.node = node;
        return this;
    }

    @Override
    public String getNode() {
        return this.node;
    }
}
