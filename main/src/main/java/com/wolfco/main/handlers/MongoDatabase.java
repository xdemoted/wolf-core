package com.wolfco.main.handlers;

import java.io.IOException;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.wolfco.main.Core;
import com.wolfco.main.classes.mongoDB.Appeal;
import com.wolfco.main.classes.mongoDB.GlobalPlayer;
import com.wolfco.main.classes.mongoDB.Punishment;

public class MongoDatabase {
    final com.mongodb.reactivestreams.client.MongoDatabase database;

    final MongoCollection<GlobalPlayer> players;
    final MongoCollection<Punishment> punishments;
    final MongoCollection<Appeal> appeals;

    final Core core;

    public MongoDatabase(Core core) throws IOException {
        this.core = core;

        String uri = "mongodb://admin:u%3FpvdhqVaCEx%23%3D'R%3B8k6sDH%7DU%3C%2FcB%2B%5BQTN%40F.nYtG-er_wfm%24*@154.29.72.40:27017/";//core.getMainConfig().getString("mongo", "null");

        if (!uri.startsWith("mongodb://"))
            throw new IOException();

        ConnectionString connectionString = new ConnectionString(uri);
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(
            PojoCodecProvider.builder()
            .register(Punishment.class)
            .automatic(true).build());
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClients.getDefaultCodecRegistry(),pojoCodecRegistry);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();

        try (MongoClient client = MongoClients.create(settings)) {
            database = client.getDatabase("WolfCo");

            players = database.getCollection("GlobalUsers", GlobalPlayer.class);
            punishments = database.getCollection("Punishments", Punishment.class);
            appeals = database.getCollection("Appeals", Appeal.class);

            System.err.println(punishments.find().first().toString());
        }
    }

    public static void main(String[] args) {
        try {
            new MongoDatabase(null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
