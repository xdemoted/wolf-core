package com.wolfco.main.handlers;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.reactivestreams.Publisher;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.reactivestreams.client.FindPublisher;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.wolfco.main.Core;
import com.wolfco.main.classes.mongoDB.Appeal;
import com.wolfco.main.classes.mongoDB.GlobalPlayer;
import com.wolfco.main.classes.mongoDB.Punishment;

import reactor.core.publisher.Flux;

public class MongoDatabase {
    final private MongoClient client;
    final private com.mongodb.reactivestreams.client.MongoDatabase database;

    final private MongoCollection<GlobalPlayer> players;
    final private MongoCollection<Punishment> punishments;
    final private MongoCollection<Appeal> appeals;

    final private Core core;

    public MongoDatabase(Core core) throws IOException {
        this.core = core;
        core.log("Connecting to MongoDB...");

        String uri = "mongodb://admin:u%3FpvdhqVaCEx%23%3D'R%3B8k6sDH%7DU%3C%2FcB%2B%5BQTN%40F.nYtG-er_wfm%24*@node.wolf-co.com:27017/";

        if (!uri.startsWith("mongodb://"))
            throw new IOException("MongoDB URI must start with mongodb://");

        ConnectionString connectionString = new ConnectionString(uri);
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(
                PojoCodecProvider.builder()
                        .register(Punishment.class, GlobalPlayer.class, Appeal.class)
                        .automatic(true).build());
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClients.getDefaultCodecRegistry(),
                pojoCodecRegistry);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();

        client = MongoClients.create(settings);

        database = client.getDatabase("WolfCo");
        players = database.getCollection("GlobalUsers", GlobalPlayer.class);
        punishments = database.getCollection("Punishments", Punishment.class);
        appeals = database.getCollection("Appeals", Appeal.class);

        Flux.from(getPlayerFlux("invalid")).doOnError(e -> {
            core.log("Error connecting to MongoDB: " + e.getMessage());
        })
                .doOnComplete(() -> {
                    core.log("Successfully connected to MongoDB");
                })
                .blockLast();
    }

    public void addPunishment(Punishment punishment) {
        Publisher<InsertOneResult> insertPublisher = punishments.insertOne(punishment);

        Flux.from(insertPublisher)
                .doOnNext(s -> {
                    core.log("Inserted punishment: " + s);
                })
                .blockLast();
    }

    public final Flux<Punishment> getPunishmentFlux(String uuid) {
        FindPublisher<Punishment> findPublisher = punishments.find(eq("uUID", uuid));
        return Flux.from(findPublisher);
    }

    public void addPlayer(GlobalPlayer player) {
        Publisher<InsertOneResult> insertPublisher = players.insertOne(player);

        Flux.from(insertPublisher)
                .doOnNext(s -> {
                    core.log("Inserted player: " + s);
                })
                .blockLast();
    }

    public void updatePlayer(GlobalPlayer player) {
        Publisher<UpdateResult> insertPublisher = players.replaceOne(eq("uUID", player.getUUID()), player);

        Flux.from(insertPublisher)
                .doOnNext(s -> {
                    core.log("Updated player: " + s);
                })
                .blockLast();
    }

    public final Flux<GlobalPlayer> getPlayerFlux(String uuid) {
        FindPublisher<GlobalPlayer> findPublisher = players.find(eq("uUID", uuid));
        return Flux.from(findPublisher);
    }

    public CompletableFuture<GlobalPlayer> getPlayer(String uuid) {
        CompletableFuture<GlobalPlayer> playerFuture = new CompletableFuture<>();

        Flux<GlobalPlayer> findPublisher = getPlayerFlux(uuid);

        AtomicBoolean doesPlayerExist = new AtomicBoolean(false);

        Flux.from(findPublisher)
                .doOnNext(s -> {
                    if (!doesPlayerExist.get()) {
                        playerFuture.complete(s);
                        doesPlayerExist.set(true);
                    }
                })
                .doOnComplete(() -> {
                    if (!doesPlayerExist.get()) {
                        playerFuture.complete(null);
                    }
                })
                .doOnError(e -> {
                    core.log("Error getting player: " + e.getMessage());
                    playerFuture.completeAsync(() -> {
                        throw new CompletionException(e);
                    });
                })
                .blockLast();

        return playerFuture;
    }

    public void addAppeal(Appeal appeal) {
        Publisher<InsertOneResult> insertPublisher = appeals.insertOne(appeal);

        Flux.from(insertPublisher)
                .doOnNext(s -> {
                    core.log("Inserted appeal: " + s);
                })
                .blockLast();
    }

    public final Flux<Appeal> getAppealFlux(String uuid) {
        FindPublisher<Appeal> findPublisher = appeals.find(eq("uUID", uuid));
        return Flux.from(findPublisher);
    }

    public void close() {
        client.close();
    }

    static public void main(String[] args) throws IOException {
        // Core core = new Core();
        MongoDatabase mongoDatabase = new MongoDatabase(null);
        mongoDatabase.close();
    }
}
