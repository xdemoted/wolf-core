package com.wolfco.main.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wolfco.main.Core;
import com.wolfco.main.classes.mongoDB.GlobalPlayer;
import com.wolfco.main.classes.redis.OnlinePlayer;
import com.wolfco.main.classes.redis.RedisListener;
import com.wolfco.main.classes.redis.messages.BaseMessage;
import com.wolfco.main.classes.redis.messages.PlayerSwitchMessage;
import com.wolfco.main.classes.redis.messages.PlayerUpdateMessage;
import com.wolfco.main.classes.redis.messages.SystemMessage;
import com.wolfco.main.events.RedisEvents;

import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

public class RedisManager {
    public String serverName;

    List<RedisListener> listeners = new ArrayList<>();
    JedisPool jedisPool;


    final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public RedisManager(String serverName) {
        this.serverName = serverName;

        String password = "";
        HostAndPort hostAndPort = getDetails(password);

        jedisPool = new JedisPool(hostAndPort, DefaultJedisClientConfig.builder()
                .password("u%3FpvdhqVaCEx%23%3D'R%3B8k6sDH%7DU%3C%2FcB%2B%5BQTN%40F.nYtG-er_wfm%24*")
                .build());

        openChannels();

        executorService.execute(() -> {
            Jedis jedis = jedisPool.getResource();
            jedis.publish("System", new SystemMessage(serverName, "online").toJson());
            jedisPool.returnResource(jedis);
        });
    }

    final public HostAndPort getDetails(String password) {
        Core core = (Core) Core.get();

        String ip = core.getMainConfig().getString("redis.ip","replace");
        int port = core.getMainConfig().getInt("redis.port",0);
        password = core.getMainConfig().getString("redis.password","replace");

        if (ip.equals("replace") || port == 0 || password.equals("replace")) {
            core.log("Redis details are not set in the config.");
            return null;
        }

        HostAndPort hostAndPort = new HostAndPort(ip, port);
        core.getLogger().log(Level.INFO, "Redis details: {0}:{1}", new Object[] { ip, port });
        return hostAndPort;
    }

    /**
     * Get a player from Redis by UUID asynchronously.
     * 
     * @param uuid The UUID of the player to get.
     * @return A CompletableFuture containing the OnlinePlayer object, or null if
     *         not found.
     * @throws JsonProcessingException If there is an error processing the JSON.
     */
    public CompletableFuture<OnlinePlayer> getPlayerAsync(String uuid) {
        return CompletableFuture.supplyAsync(() -> getPlayer(uuid), executorService);
    }

    private OnlinePlayer getPlayer(String uuid) {
        try (Jedis jedis = jedisPool.getResource()) {
            if (jedis.exists(uuid)) {
                String playerData = jedis.get(uuid);
                jedisPool.returnResource(jedis);

                ObjectMapper objectMapper = new ObjectMapper();
                OnlinePlayer player = objectMapper.readValue(playerData, OnlinePlayer.class);

                return player;
            } else {
                jedisPool.returnResource(jedis);
                return null;
            }
        } catch (JsonProcessingException e) {
            Core.get().getLogger().log(Level.WARNING, "Failed to convert JSON to player: {0}", e.getMessage());
            return null;
        }
    }

    public void setPlayerAsync(GlobalPlayer player) {
        executorService.execute(() -> addPlayer(player));
    }

    private void addPlayer(GlobalPlayer player) {
        ObjectMapper objectMapper = new ObjectMapper();

        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(player.getUUID(), objectMapper.writeValueAsString(player));
            jedisPool.returnResource(jedis);
        } catch (JsonProcessingException e) {
            Core.get().getLogger().log(Level.WARNING, "Failed to convert player to JSON: {0}", e.getMessage());
        }
    }

    public void removePlayerAsync(String uuid) {
        executorService.execute(() -> removePlayer(uuid));
    }

    private void removePlayer(String uuid) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(uuid);
            jedisPool.returnResource(jedis);
        }
    }

    // Server Messaging

    public void sendMessage(String channel, BaseMessage message) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.publish(channel, message.toJson());
            jedisPool.returnResource(jedis);
        }
    }

    public void sendPlayerJoinAsync(GlobalPlayer player) {
        executorService.execute(() -> sendPlayerJoinEvent(player.getUUID(), player.getDiscordID(),
                player.getNickname()));
    }

    public void sendPlayerJoinAsync(String uuid, String discordID, String nickname) {
        executorService.execute(() -> sendPlayerJoinEvent(uuid, discordID, nickname));
    }

    private void sendPlayerJoinEvent(String uuid, String discordID, String nickname) {
        PlayerUpdateMessage playerUpdateMessage = new PlayerUpdateMessage(serverName, uuid, discordID, nickname);
        sendMessage("PlayerJoin", playerUpdateMessage);
    }

    public void sendPlayerLeaveAsync(GlobalPlayer player) {
        executorService.execute(() -> sendPlayerLeaveEvent(player.getUUID(), player.getDiscordID(),
                player.getNickname()));
    }

    public void sendPlayerLeaveAsync(String uuid, String discordID, String nickname) {
        executorService.execute(() -> sendPlayerLeaveEvent(uuid, discordID, nickname));
    }

    private void sendPlayerLeaveEvent(String uuid, String discordID, String nickname) {
        PlayerUpdateMessage playerUpdateMessage = new PlayerUpdateMessage(serverName, uuid, discordID, nickname);
        sendMessage("PlayerLeave", playerUpdateMessage);
    }

    public void sendPlayerSwitchAsync(GlobalPlayer player, String oldServer) {
        executorService.execute(() -> sendPlayerSwitchEvent(player.getUUID(), player.getDiscordID(),
                player.getNickname(), oldServer));
    }

    public void sendPlayerSwitchAsync(String uuid, String discordID, String nickname, String oldServer) {
        executorService.execute(() -> sendPlayerSwitchEvent(uuid, discordID, nickname, oldServer));
    }

    private void sendPlayerSwitchEvent(String uuid, String discordID, String nickname, String oldServer) {
        PlayerSwitchMessage playerSwitchMessage = new PlayerSwitchMessage(serverName, oldServer, uuid, discordID,
                nickname);
        sendMessage("PlayerSwitch", playerSwitchMessage);
    }

    public void sendSystemMessageAsync(String message) {
        executorService.execute(() -> sendSystemMessage(message));
    }

    public void sendSystemMessage(String message) {
        SystemMessage systemMessage = new SystemMessage(serverName, message);
        sendMessage("System", systemMessage);
    }

    private void openChannels() {
        RedisEvents.createAndRegister(this);

        executorService.execute(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        BaseMessage redisMessage = BaseMessage.fromJson(message, BaseMessage.class);
                        if (redisMessage.getServerName().equals(serverName))
                            return;

                        emit(channel, message);
                    }

                    @Override
                    public void onUnsubscribe(String channel, int subscribedChannels) {
                        jedisPool.returnResource(jedis);
                    }
                }, "System", "PlayerJoin", "PlayerLeave", "PlayerSwitch");
            }
        });
    }

    public void addListener(RedisListener listener) {
        listeners.add(listener);
    }

    public void emit(String channel, String message) {
        for (RedisListener listener : listeners) {
            switch (channel) {
                case "System" -> listener.onSystemMessage(message);
                case "PlayerJoin" -> listener.onPlayerJoin(message);
                case "PlayerLeave" -> listener.onPlayerLeave(message);
                case "PlayerSwitch" -> listener.onPlayerSwitch(message);
            }
        }
    }

    public void close() {
        jedisPool.close();
    }

    static public void main(String[] args) throws InterruptedException {
        RedisManager redisManager = new RedisManager("TestServer");

        Thread.sleep(5000);

        redisManager.sendMessage("System", new SystemMessage("Test2", "online"));
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
