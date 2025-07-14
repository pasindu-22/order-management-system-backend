package com.ecommerce.user_service.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig {
    @Value("${mongodb.uri:mongodb://localhost:27017}")
    private String mongoUri;

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, "userservice_db");
    }

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(mongoUri);
    }
}
