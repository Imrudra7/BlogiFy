package com.stranger.blogify.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.data.mongodb.core.MongoTemplate;

@Component
public class MongoConnectionCheck implements CommandLineRunner {

    private final MongoTemplate mongoTemplate;

    public MongoConnectionCheck(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void run(String... args) {
        try {
            mongoTemplate.getDb().listCollectionNames().first();
            System.out.println("✅ MongoDB connection successful!");
        } catch (Exception e) {
            System.err.println("❌ MongoDB connection failed: " + e.getMessage());
        }
    }
}
