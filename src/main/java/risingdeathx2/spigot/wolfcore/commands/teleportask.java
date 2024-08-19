package risingdeathx2.spigot.wolfcore.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import risingdeathx2.spigot.wolfcore.core;
import risingdeathx2.spigot.wolfcore.utils;
import risingdeathx2.spigot.wolfcore.classes.Argument;
import risingdeathx2.spigot.wolfcore.classes.ArgumentType;
import risingdeathx2.spigot.wolfcore.classes.Command;
import risingdeathx2.spigot.wolfcore.classes.CoreCommandExecutor;
import risingdeathx2.spigot.wolfcore.classes.PlayerData;

public class teleportask implements CoreCommandExecutor {
    HashMap<String, String> requestTypes = new HashMap<>() {{
        put("teleportask", "tpa");
        put("tpa", "tpa");
        put("tpahere", "tpahere");
        put("teleportaskhere", "tpahere");
    }};
    @Override
    public Command getCommand() {
        return new Command("teleportask", new ArrayList<>() {{
            add(new Argument("player", ArgumentType.PLAYER, false));
        }});
    }
    core core;
    public teleportask(core core) {
        this.core = core;
    }
    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            utils.sendColorText(core.adventure().sender(sender), core.getMessage("generic.noconsole"));
            return false;
        }
        Player receiver = getCommand().options.get(0).getExclusivePlayer(core, args[0]);
        if (receiver == null) {
            utils.sendColorText(core.adventure().sender(sender), core.getMessage("generic.playernotfound"));
            return false;
        }
        PlayerData receiverData = core.playerManager.getPlayerData(receiver);
        if (receiverData == null) {
            utils.sendColorText(core.adventure().sender(sender), core.getMessage("generic.playernotfound"));
            return false;
        }
        if (receiverData.lastRequest.host == sender&&System.currentTimeMillis() - receiverData.lastRequest.startTime < 30000) {
            utils.sendColorText(core.adventure().sender(sender), core.getMessage("teleportask.existing"));
            return false;
        } else {
            receiverData.sendRequest((Player) sender, requestTypes.get(command.getName()));
            utils.sendColorText(core.adventure().sender(sender), core.getMessage("teleportask.sent", List.of(receiver.getName())));
            if (receiverData.lastRequest.type.equalsIgnoreCase("tpa")) {
                utils.sendColorText(core.adventure().sender(receiver), core.getMessage("teleportask.received", List.of(sender.getName())));
            } else if (receiverData.lastRequest.type.equalsIgnoreCase("tpahere")) {
                utils.sendColorText(core.adventure().sender(receiver), core.getMessage("teleportask.receivedhere", List.of(sender.getName())));
            }
            return true;
        }
    }
}
// Messages need added to the messages.yml file
