package risingdeathx2.spigot.wolfcore.commands;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.kyori.adventure.audience.Audience;
import net.luckperms.api.model.user.User;
import risingdeathx2.spigot.wolfcore.core;
import risingdeathx2.spigot.wolfcore.utils;
import risingdeathx2.spigot.wolfcore.classes.Argument;
import risingdeathx2.spigot.wolfcore.classes.ArgumentType;
import risingdeathx2.spigot.wolfcore.classes.Command;
import risingdeathx2.spigot.wolfcore.classes.CoreCommandExecutor;
import risingdeathx2.spigot.wolfcore.utilities.PermissionHandler;

public class max implements CoreCommandExecutor {
    @Override
    public Command getCommand() {
        return new Command("max","wolfcore.max", new ArrayList<>() {{
            add(new Argument("node", ArgumentType.STRING, false));
        }});
    }

    core core;
    public max(core core) {
        this.core = core;
    }
    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        Audience audience = core.adventure().sender(sender);
        User user = core.lp.getUserManager().getUser(sender.getName());
        if (!(sender instanceof Player)) {
            utils.sendColorText(audience, core.getMessage("generic.noconsole"));
            return true;
        }
        utils.sendColorText(audience, "Max is " + PermissionHandler.getNumberValue(args[0], user));
        return true;
    }
    
}
