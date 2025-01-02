package com.wolfco.common.classes.argumenthandlers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.wolfco.common.classes.ArgumentInterface;
import com.wolfco.common.classes.CorePlugin;

public class NumberArg implements ArgumentInterface {

    final boolean required;
    String name = "NUMBER";
    double min = -1;
    double max = -1;
    int accuracy = 0;

    public NumberArg(boolean required, int accuracy) {
        this.required = required;
        this.accuracy = accuracy;
    }

    public NumberArg setAccuracy(int accuracy) {
        this.accuracy = accuracy;
        return this;
    }

    public double evalAccuracy(double input) {
        return Math.round(input * Math.pow(10, accuracy)) / Math.pow(10, accuracy);
    }

    public NumberArg setConstraints(double min, double max) {
        this.min = evalAccuracy(min);
        this.max = evalAccuracy(max);

        if (max - min < 0) {
            throw new IllegalArgumentException("Max value must be greater than min value.");
        }

        return this;
    }

    public double evalConstraints(double input) {

        if (min > 0 && input < min) {
            return min;
        } else if (max > 0 && input > max) {
            return max;
        }

        return input;
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
        List<String> options = new ArrayList<>();
        String formatCode = "%." + accuracy + "f";

        if (max - min > 6) {
            for (int i = 0; i < 3; i++) {
                options.add(String.format(formatCode,max - i));
            }
            for (int i = 0; i < 3; i++) {
                options.add(String.format(formatCode,max - i));
            }
        } else {
            for (double i = min; i <= max; i++) {
                options.add(String.format(formatCode, i));
            }
        }

        return options;
    }

    @Override
    public Double getValue(CorePlugin core, CommandSender sender, Command bukkitCommand, String searchValue) {
        double value;

        String formatCode = "%." + accuracy + "f";
        String minString = String.format(formatCode, min);
        String maxString = String.format(formatCode, max);
        String type = "number";

        if (accuracy == 0) name = "integer";

        try {
            value = Double.parseDouble(searchValue);
        } catch (NumberFormatException e) {
            throw error("Argument %s requires a valid %s between %s and %s", name, type, minString, maxString);
        }

        if (evalConstraints(value) != value) {
            throw error("Argument %s must be a number between %s and %s", name, type, minString, maxString);
        } else {
            return evalAccuracy(value);
        }
    }

}
