package com.wolfco.main.events;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONException;

import com.wolfco.main.Core;
import com.wolfco.main.classes.redis.AsyncGlobalMessageEvent;
import com.wolfco.main.classes.redis.BaseMessage;
import com.wolfco.main.classes.redis.ChatMessage;

import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

public class RedisManager {
    private static RedisManager instance = null;

    public String serverName;
    Core core = (Core) Core.get();
    JedisPool jedisPool;
    Jedis publisher;
    String password;

    final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public RedisManager(String serverName) {
        core.log("Redis Initialization!");
        this.serverName = serverName;
        HostAndPort hostAndPort = getDetails();
        jedisPool = new JedisPool(hostAndPort, DefaultJedisClientConfig.builder()
                .password(password)
                .build());

        // create a dedicated publisher client separate from subscription resource(s)
        publisher = new Jedis(hostAndPort, DefaultJedisClientConfig.builder()
                .password(password)
                .build());

        openChannels();

        // publish initial online message using the dedicated publisher
        executorService.execute(() -> {
            try {
                core.log("Redis Publish!");
                publisher.publish("System", new BaseMessage(serverName, "online").toJson());
            } catch (Exception e) {
                core.log("Failed to publish initial Redis message: " + e.getMessage());
            }
        });
    }

    public void restartRedis() {
        core.log("Restarting Redis connection...");
        close();
        HostAndPort hostAndPort = getDetails();
        jedisPool = new JedisPool(hostAndPort, DefaultJedisClientConfig.builder()
                .password(password)
                .build());

        publisher = new Jedis(hostAndPort, DefaultJedisClientConfig.builder()
                .password(password)
                .build());

        openChannels();

        executorService.execute(() -> {
            try {
                core.log("Redis Publish!");
                publisher.publish("System", new BaseMessage(serverName, "online").toJson());
            } catch (Exception e) {
                core.log("Failed to publish initial Redis message: " + e.getMessage());
            }
        });
    }

    final public HostAndPort getDetails() {
        String ip = core.getMainConfig().getString("redis.ip", "replace");
        int port = core.getMainConfig().getInt("redis.port", 0);
        password = core.getMainConfig().getString("redis.password", "replace");

        if (ip.equals("replace") || port == 0 || password.equals("replace")) {
            core.log("Redis details are not set in the config.");
            return null;
        }

        HostAndPort hostAndPort = new HostAndPort(ip, port);
        core.getLogger().log(Level.INFO, "Redis details: {0}:{1}", new Object[] { ip, port });
        core.getLogger().log(Level.INFO, "Redis password: {0}", password);
        return hostAndPort;
    }

    public void sendMessage(String channel, BaseMessage message) {
        try {
            publisher.publish(channel, message.toJson());
        } catch (Exception e) {
            core.log("Failed to publish Redis message: " + e.getMessage());
        }
    }

    public void sendMessageAsync(String channel, BaseMessage message) {
        executorService.execute(() -> sendMessage(channel, message));
    }

    public void sendChatMessageAsync(ChatMessage chatMessage) {
        executorService.execute(() -> sendMessage("ChatMessage", chatMessage));
    }

    public void sendChatMessageAsync(Player player, String message) {
        executorService.execute(() -> sendChatMessage(player, message));
    }

    void sendChatMessage(Player player, String message) {
        ChatMessage chatMessage = new ChatMessage(serverName, player, message);
        sendMessage("ChatMessage", chatMessage);
    }

    public void sendSystemMessageAsync(String message) {
        executorService.execute(() -> {
            BaseMessage systemMessage = new BaseMessage(serverName, message);
            sendMessage("System", systemMessage);
        });
    }

    public void sendJoin(Player player) {
        BaseMessage joinMessage = new BaseMessage(serverName, player.getUniqueId().toString());
        sendMessage("PlayerJoin", joinMessage);
    }

    public void sendQuit(Player player) {
        BaseMessage quitMessage = new BaseMessage(serverName, player.getUniqueId().toString());
        sendMessage("PlayerQuit", quitMessage);
    }

    public void sendSwitchServer(Player player, String targetServer) {
        BaseMessage switchMessage = new BaseMessage(serverName,
                player.getUniqueId().toString() + ";" + targetServer);
        sendMessage("PlayerSwitch", switchMessage);
    }

    private void openChannels() {
        executorService.execute(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        BaseMessage redisMessage;
                        try {
                            redisMessage = BaseMessage.fromJson(message);
                        } catch (JSONException e) {
                            core.log("Failed to parse Redis message: " + e.getMessage());
                            return;
                        }

                        if (core.getServerName().equals(redisMessage.serverName))
                            return;

                        core.log(channel + " " + message);

                        switch (channel) {
                            case "System" -> {
                                core.log("[" + channel + "] Message from " + redisMessage.serverName + ": "
                                        + redisMessage.data);
                            }
                            case "ChatMessage" -> {
                                AsyncGlobalMessageEvent event = new AsyncGlobalMessageEvent(
                                        ChatMessage.fromJson(message));
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        core.getServer().getPluginManager().callEvent(event);
                                    }
                                }.run();
                            }
                            default -> {
                                core.log("Unknown Redis message channel: " + channel);
                            }
                        }
                        core.log("Received message on channel " + channel + " from server " + redisMessage.serverName);
                    };
                }, "System", "ChatMessage", "PlayerJoin", "PlayerQuit");
            }
        });
    };

    public void close() {
        try {
            if (publisher != null) {
                publisher.close();
            }
        } catch (Exception ignored) {
        }
        jedisPool.close();
    }

    public static RedisManager getInstance() {
        if (instance == null) {
            instance = getInstance(((Core) Core.get()).getServerName());
        }

        return instance;
    }

    public static RedisManager getInstance(String serverName) {
        instance = new RedisManager(serverName);

        return instance;
    }
}