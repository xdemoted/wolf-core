package com.wolfco;

import java.util.ArrayList;
import java.util.List;

import org.bson.codecs.configuration.CodecProvider;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.wolfco.types.Program;
import com.wolfco.types.Project;

public class MongoConnector {

    MongoDatabase database;
    MongoCollection<Project> projects;
    MongoClient client;
    String uri = "mongodb://localhost:27017/";
    String databaseName = "projectDB";

    public MongoConnector(String uri, String databaseName) {
        this.uri = uri;
        this.databaseName = databaseName;
    }

    public boolean connect() {
        try {
            client = MongoClients.create(uri);

            CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
            CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(),
                    fromProviders(pojoCodecProvider));
            database = client.getDatabase(databaseName).withCodecRegistry(pojoCodecRegistry);
            projects = database.getCollection("projects", Project.class);
            System.out.println("Connected to the database");
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public Project getProject(String name) {
        return (Project) projects.find().first();

    }

    public boolean addProject(Project project) {
        Bson filter = Filters.eq("name", project.getName());

        if (projects.find(filter).first() != null) {
            return false;
        }

        projects.insertOne(project);
        return true;
    }

    public List<Project> listProjects() {
        return projects.find().into(new ArrayList<>());
    }

    public List<Program> listPrograms() {
        List<Program> programs = new ArrayList<>();

        for (Project project : listProjects()) {
            Program program;

            if (programs.stream().noneMatch(p -> p.getName().equals(project.getParent()))) {
                program = new Program();
                program.setName(project.getParent());
                programs.add(program);
            } else {
                program = programs.stream().filter(p -> p.getName().equals(project.getParent())).findFirst().get();
            }

            program.addProject(project);
        }

        return programs;
    }
}
