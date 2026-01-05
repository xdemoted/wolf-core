package com.wolfco.main.commands;

import java.util.List;

import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommand;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.common.commands.arguments.BooleanArg;
import com.wolfco.common.commands.arguments.PlayerArg;
import com.wolfco.main.Core;
import com.wolfco.main.utility.FontUtil;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class Fly implements CoreCommand {
    @Override
    public Command getCommand() {
        Command command = new Command().setName("fly");
        command.setAccessType(AccessType.PLAYER);
        command.addArguments(
                new PlayerArg(false).includeSender(false),
                new BooleanArg(false).setName("TOGGLE"));

        return command;
    }

    @Inject
    Core core;

    @Override
    public boolean onCommand(CommandSourceStack commandStack, String[] args,
            Object[] argumentValues) {
        Player target = (Player) argumentValues[0];
        Player sender = (Player) commandStack.getSender();

        if (!(target instanceof Player)) {
            target = sender;
        } else if (!sender.hasPermission("wolfcore.fly.others")) {
            core.sendPreset(sender, "generic.nopermission");
            return false;
        }

        boolean toggle;

        if (argumentValues[1] == null) {
            toggle = !target.getAllowFlight();
        } else {
            toggle = (boolean) argumentValues[1];
        }

        target.setAllowFlight(toggle);
        target.setFlying(toggle);

        if (sender != target) {
            core.sendPreset(sender, "fly.othersuccess",
                    List.of(toggle ? "enabled" : "disabled", FontUtil.getPlayerTag(target)));
            core.sendPreset(target, "fly.success", List.of(toggle ? "enabled" : "disabled"));
        } else {
            core.sendPreset(sender, "fly.success", List.of(toggle ? "enabled" : "disabled"));
        }

        return true;
    }
}
