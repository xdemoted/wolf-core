package com.wolfco.main.events;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONException;

import com.wolfco.main.Core;
import com.wolfco.main.classes.redis.BaseMessage;
import com.wolfco.main.classes.redis.ChatMessage;
import com.wolfco.main.classes.redis.GlobalMessageEvent;

import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

public class RedisManager {
    public String serverName;
    Core core = (Core) Core.get();
    JedisPool jedisPool;
    final Jedis publisher;
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
            synchronized (publisher) {
                try {
                    core.log("Redis Publish!");
                    publisher.publish("System", new BaseMessage(serverName, "online").toJson());
                } catch (Exception e) {
                    core.log("Failed to publish initial Redis message: " + e.getMessage());
                }
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
        publisher.publish(channel, message.toJson());
    }

    public void sendChatMessageAsync(String formatData, String messageData, Boolean colorEnabled) {
        executorService.execute(() -> sendChatMessage(formatData, messageData, colorEnabled));
    }

    public void sendChatMessage(String formatData, String messageData, Boolean colorEnabled) {
        ChatMessage chatMessage = new ChatMessage(serverName, formatData, messageData);
        if (colorEnabled)
            chatMessage.enableColor();
        sendMessage("ChatMessage", chatMessage);
    }

    public void sendSystemMessageAsync(String message) {
        executorService.execute(() -> sendSystemMessage(message));
    }

    public void sendSystemMessage(String message) {
        BaseMessage systemMessage = new BaseMessage(serverName, message);
        sendMessage("System", systemMessage);
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
                                GlobalMessageEvent event = new GlobalMessageEvent(ChatMessage.fromJson(message));
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
                }, "System", "ChatMessage");
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
}