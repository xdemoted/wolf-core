package com.wolfco.main.events;

import java.util.List;

import org.bukkit.entity.Player;

import com.wolfco.common.Utilities;
import com.wolfco.main.Core;
import com.wolfco.main.classes.redis.RedisListener;
import com.wolfco.main.classes.redis.messages.BaseMessage;
import com.wolfco.main.classes.redis.messages.SystemMessage;
import com.wolfco.main.handlers.RedisManager;

public class RedisEvents implements RedisListener {
    String serverName;

    @Override
    public void onSystemMessage(String message) {
        List<Player> staff = Utilities.getStaff(Core.get());

        SystemMessage formattedMessage = BaseMessage.fromJson(message, SystemMessage.class);

        switch (formattedMessage.getMessage()) {
            case "online" -> {
                if (formattedMessage.getServerName().equals(serverName)) {
                    return;
                }

                for (Player player : staff) {
                    player.sendMessage(String.format("§8[§4Network§8]§c Server %s is online.", formattedMessage.getServerName()));
                }
            }
            case "offline" -> {
                if (formattedMessage.getServerName().equals(serverName)) {
                    ((Core) Core.get()).getRedisManager().close();
                    return;
                }

                for (Player player : staff) {
                    player.sendMessage("§8[§4System§8]§c Server " + formattedMessage.getServerName() + " is offline.");
                }
            }
        
            default -> {
            }
        }
    }

    @Override
    public void onPlayerJoin(String message) {
        
    }

    @Override
    public void onPlayerLeave(String message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onPlayerLeave'");
    }

    @Override
    public void onPlayerSwitch(String message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onPlayerSwitch'");
    }
    
    public static RedisEvents createAndRegister(RedisManager redisManager) {
        RedisEvents redisEvents = new RedisEvents();
        redisManager.addListener(redisEvents);

        redisEvents.serverName = redisManager.serverName;
        
        return redisEvents;
    }
}
