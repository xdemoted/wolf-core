package com.wolfco.main.commands;

import java.util.HashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolfco.common.classes.Command;
import com.wolfco.common.classes.CoreCommandExecutor;
import com.wolfco.common.classes.argumenthandlers.PlayerArg;
import com.wolfco.common.classes.types.AccessType;
import com.wolfco.main.Core;

public class TeleportAsk implements CoreCommandExecutor {

    HashMap<String, String> requestTypes = new HashMap<>();
    static final String TPA = "tpa";
    static final String TPAHERE = TPA + "here";

    public TeleportAsk() {
        requestTypes.put("teleportask", TPA);
        requestTypes.put(TPA, TPA);
        requestTypes.put(TPAHERE, TPAHERE);
        requestTypes.put("teleportaskhere", TPAHERE);
    }

    @Override
    public Command getCommand() {
        Command command = new Command("teleportask");
        command.setAccessType(AccessType.PLAYER);
        command.addArguments(new PlayerArg(true).includeSender(false));

        return command;
    }

    @Override
    public Core fetchCore() {
        return core;
    }

    Core core;

    public TeleportAsk(Core core) {
        this.core = core;
    }

    // TODO: Finish teleport ask command

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, Object[] argumentValues) {
        Player receiver = (Player) argumentValues[0];
        var receiverData = core.getPlayerManager().getPlayerData(receiver);

        return true;
    }
}
