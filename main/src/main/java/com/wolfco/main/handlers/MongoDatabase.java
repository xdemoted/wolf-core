package com.wolfco.main.handlers;

import java.io.IOException;
import java.util.List;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.reactivestreams.Publisher;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.reactivestreams.client.FindPublisher;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.wolfco.main.Core;
import com.wolfco.main.classes.mongoDB.Appeal;
import com.wolfco.main.classes.mongoDB.GlobalPlayer;
import com.wolfco.main.classes.mongoDB.Punishment;
import com.wolfco.main.classes.mongoDB.subtypes.Address;

import reactor.core.publisher.Flux;

public class MongoDatabase {
    final com.mongodb.reactivestreams.client.MongoDatabase database;

    final MongoCollection<GlobalPlayer> players;
    final MongoCollection<Punishment> punishments;
    final MongoCollection<Appeal> appeals;

    final Core core;

    boolean working = true;

    public MongoDatabase(Core core) throws IOException {
        this.core = core;

        System.out.println("Connecting to MongoDB...");

        String uri = "mongodb://admin:u%3FpvdhqVaCEx%23%3D'R%3B8k6sDH%7DU%3C%2FcB%2B%5BQTN%40F.nYtG-er_wfm%24*@154.29.72.40:27017/";
        if (!uri.startsWith("mongodb://"))
            throw new IOException();

        ConnectionString connectionString = new ConnectionString(uri);
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(
                PojoCodecProvider.builder()
                        .register(Punishment.class)
                        .automatic(true).build());
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClients.getDefaultCodecRegistry(),
                pojoCodecRegistry);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();

        try (MongoClient client = MongoClients.create(settings)) {
            database = client.getDatabase("WolfCo");

            players = database.getCollection("GlobalUsers", GlobalPlayer.class);
            punishments = database.getCollection("Punishments", Punishment.class);
            appeals = database.getCollection("Appeals", Appeal.class);

            GlobalPlayer player = new GlobalPlayer()
            .setUUID("2cbf357d-5038-4444-a868-e4dfd6c7538b")
            .setDiscordID("123456789")
            .setNickname("Test")
            .setLastLogin(System.currentTimeMillis())
            .setLastLogout(System.currentTimeMillis())
            .setPunishments(List.of())
            .setAddresses(List.of(
                new Address()
                .setIp(uri)
                .setLastUsed(System.currentTimeMillis())
                .setUses(1)
            ))
            .setUsernames(List.of("Demoted__"))
            .setFriends(List.of("2cbf357d-5038-4444-a868-e4dfd6c7538b"));


            Publisher<InsertOneResult> insertPublisher = players.insertOne(player);

            Flux.from(insertPublisher)
                    .doOnNext(s -> {
                        System.out.println("Inserted player: " + s);
                    })
                    .blockLast();

            FindPublisher<GlobalPlayer> findPublisher = players
                    .find(eq("uuid", "2cbf357d-5038-4444-a868-e4dfd6c7538b"));

            Flux.from(findPublisher)
                    .doOnNext(s -> {
                        System.out.println("Found punishment: " + s.getNickname());
                    })
                    .blockLast();
        }
    }

    public void addPunishment(Punishment punishment) {
        Publisher<InsertOneResult> insertPublisher = punishments.insertOne(punishment);

        Flux.from(insertPublisher)
                .doOnNext(s -> {
                    System.out.println("Inserted punishment: " + s);
                })
                .blockLast();
    }

    public Flux<Punishment> getPunishment(String uuid) {
        FindPublisher<Punishment> findPublisher = punishments.find(eq("uuid", uuid));
        return Flux.from(findPublisher);
    }

    public static void main(String[] args) {
        MongoDatabase db = null;
        try {
            db = new MongoDatabase(null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int i = 0; // Keep async thread alive
        while (i < 10) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
        }

    }
}
