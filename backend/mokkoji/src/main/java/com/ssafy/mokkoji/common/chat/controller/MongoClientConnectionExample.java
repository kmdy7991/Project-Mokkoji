package com.ssafy.mokkoji.common.chat.controller;


import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.*;
import org.bson.Document;

import java.util.Collections;
import java.util.List;

public class MongoClientConnectionExample {
    public static void main(String[] args) {
        String connectionString = "mongodb+srv://S10P13B304:zyolRrePbj@ssafy.ngivl.mongodb.net/S10P13B304?authSource=admin&retryWrites=true&w=majority";

        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();

        // Create a new client and connect to the server
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            try {
                // Send a ping to confirm a successful connection
                MongoDatabase database = mongoClient.getDatabase("admin");
                database.runCommand(new Document("ping", 1));
                System.out.println(database.getCollection("Mokkoji").find());
                System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
            } catch (MongoException e) {
                e.printStackTrace();
            }
        }
        // Create a new client and connect to the server
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            try {
                // Send a ping to confirm a successful connection
                MongoDatabase database = mongoClient.getDatabase("S10P13B304");
                MongoCollection<Document> collection = database.getCollection("Mokkoji");

                // Find all documents in the collection
                FindIterable<Document> documents = collection.find();

                // Iterate over the documents and print them
                for (Document document : documents) {
                    System.out.println(document.toJson());
                }

                System.out.println("Successfully retrieved documents from MongoDB!");
            } catch (MongoException e) {
                e.printStackTrace();
            }
        }

    }
}
