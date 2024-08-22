package risingdeathx2.spigot.wolfcore.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import risingdeathx2.spigot.wolfcore.core;
import risingdeathx2.spigot.wolfcore.utils;
import risingdeathx2.spigot.wolfcore.classes.Command;
import risingdeathx2.spigot.wolfcore.classes.CoreCommandExecutor;
import risingdeathx2.spigot.wolfcore.classes.PlayerData;

public class teleportdeny implements CoreCommandExecutor {

    @Override
    public Command getCommand() {
        return new Command("teleportdeny","wolfcore.tpdeny", new ArrayList<>());
    }

    core core;

    public teleportdeny(core core) {
        this.core = core;
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        if (sender instanceof Player) {
            PlayerData playerData = core.playerManager.getPlayerData((Player) sender);
            if (playerData == null) {
                utils.sendColorText(core.adventure().sender(sender), core.getMessage("generic.invaliddata"));
                return false;
            }
            if (playerData.lastRequest == null) {
                utils.sendColorText(core.adventure().sender(sender), core.getMessage("teleportask.norequest"));
                return false;
            }
            utils.sendColorText(core.adventure().sender(sender),
                    core.getMessage("teleportask.deny", List.of(playerData.lastRequest.host.getName())));
            utils.sendColorText(core.adventure().sender(playerData.lastRequest.host),
                    core.getMessage("teleportask.deny", List.of(sender.getName())));
            playerData.lastRequest = null;
            return true;
        } else {
            utils.sendColorText(core.adventure().sender(sender), core.getMessage("generic.noconsole"));
            return false;
        }
    }

}
