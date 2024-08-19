package risingdeathx2.spigot.wolfcore.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import risingdeathx2.spigot.wolfcore.core;
import risingdeathx2.spigot.wolfcore.utils;
import risingdeathx2.spigot.wolfcore.classes.Command;
import risingdeathx2.spigot.wolfcore.classes.CoreCommandExecutor;
import risingdeathx2.spigot.wolfcore.classes.PlayerData;

public class teleportaccept implements CoreCommandExecutor {
    public Command getCommand() {
        return new Command("teleportaccept", new ArrayList<>());
    }

    core core;
    public teleportaccept(risingdeathx2.spigot.wolfcore.core core) {
        this.core = core;
    }

    @Override
    public boolean execute(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            utils.sendColorText(core.adventure().sender(sender), core.getMessage("generic.noconsole"));
            return false;
        }
        Player receiver = (Player) sender;
        PlayerData receiverData = core.playerManager.getPlayerData(receiver);
        if (receiverData == null) {
            utils.sendColorText(core.adventure().sender(sender), core.getMessage("generic.invaliddata"));
            return false;
        }
        if (receiverData.lastRequest == null) {
            utils.sendColorText(core.adventure().sender(sender), core.getMessage("teleportask.norequest"));
            return false;
        }
        if (receiverData.lastRequest.type.equalsIgnoreCase("tpa")) {
            receiverData.lastRequest.host.teleport(receiver);
            utils.sendColorText(core.adventure().sender(receiverData.lastRequest.host), core.getMessage("teleportask.teleporting", List.of(receiver.getName())));
            utils.sendColorText(core.adventure().sender(receiver), core.getMessage("teleportask.accept", List.of(receiverData.lastRequest.host.getName())));
        } else if (receiverData.lastRequest.type.equalsIgnoreCase("tpahere")) {
            receiver.teleport(receiverData.lastRequest.host);
            utils.sendColorText(core.adventure().sender(receiverData.lastRequest.host), core.getMessage("teleportask.accept", List.of(receiver.getName())));
            utils.sendColorText(core.adventure().sender(receiver), core.getMessage("teleportask.teleporting", List.of(receiverData.lastRequest.host.getName())));
        }
        return true;
    }
}
