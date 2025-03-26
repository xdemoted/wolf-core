package com.wolfco.main.handlers;

import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

public class RedisManager {
    JedisPool jedisPool;

    public RedisManager() {
        HostAndPort hostAndPort = new HostAndPort("154.29.72.40", 6379);
        jedisPool = new JedisPool(hostAndPort, DefaultJedisClientConfig.builder()
                .password("u?pvdhqVaCEx#='R;8k6sDH}U</cB+[QTN@F.nYtG-er_wfm$*")
                .build());

        openChannels();

        Jedis jedis = jedisPool.getResource();

        jedis.publish("SystemMessageEvent", "Server1 is online");

        jedisPool.returnResource(jedis);
    }

    public void sendPlayerJoinEvent(String playerName) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.publish("PlayerJoinEvent", playerName);
        }
    }

    private void openChannels() {
        new Thread() {
            @Override
            public void run() {
                try (Jedis jedis = jedisPool.getResource()) {
                    jedis.subscribe(new JedisPubSub() {
                        @Override
                        public void onMessage(String channel, String message) {
                            System.err.println("Channel: " + channel + " | Message: " + message);
                            switch (channel) {
                                case "SystemMessageEvent" -> {

                                }
                            }
                        }

                        @Override
                        public void onUnsubscribe(String channel, int subscribedChannels) {
                            jedisPool.returnResource(jedis);
                        }
                    }, "SystemMessageEvent", "PlayerJoinEvent", "PlayerLeaveEvent", "PlayerSwitchEvent");
                }
            }
        }.start();
    }

    public static void main(String[] args) {
        new RedisManager();
    }
}
