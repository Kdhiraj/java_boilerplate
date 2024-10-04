package com.eventline.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class MongoConfig {

    // Load values from application.properties
    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Value("${spring.data.mongodb.pool.min-size:10}") // Default min pool size is 10
    private int minPoolSize;

    @Value("${spring.data.mongodb.pool.max-size:100}") // Default max pool size is 100
    private int maxPoolSize;

    @Value("${spring.data.mongodb.timeout.connection-timeout:3000}") // Default connection timeout 3 seconds
    private int connectionTimeout;

    @Value("${spring.data.mongodb.timeout.socket-timeout:3000}") // Default socket timeout 3 seconds
    private int socketTimeout;

    @Value("${spring.data.mongodb.timeout.max-wait-time:120000}") // Default max wait time 2 minutes
    private int maxWaitTime;

    @Value("${spring.data.mongodb.ssl.enabled:false}") // SSL can be toggled via properties
    private boolean sslEnabled;

    /**
     * MongoClient bean creation with connection pooling, timeouts, and SSL settings.
     * @return MongoClient
     */
    @Bean
    public MongoClient mongoClient() {
        try {
            ConnectionString connectionString = new ConnectionString(mongoUri);

            MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .applyToConnectionPoolSettings(builder ->
                            builder.maxSize(maxPoolSize) // Max pool size
                                    .minSize(minPoolSize)  // Min pool size
                                    .maxWaitTime(maxWaitTime, TimeUnit.MILLISECONDS)) // Max wait time for a connection
                    .applyToSocketSettings(builder ->
                            builder.connectTimeout(connectionTimeout, TimeUnit.MILLISECONDS) // Connection timeout
                                    .readTimeout(socketTimeout, TimeUnit.MILLISECONDS)) // Socket timeout
                    .applyToSslSettings(builder -> builder.enabled(sslEnabled)) // SSL enabled or disabled
                    .build();

            MongoClient mongoClient = MongoClients.create(mongoClientSettings);
            log.info("MongoDB connection established successfully with URI: {}", mongoUri);
            return mongoClient;
        } catch (Exception e) {
            log.error("Failed to connect to MongoDB at {}: {}", mongoUri, e.getMessage());
            throw new RuntimeException("MongoDB connection failed", e);
        }
    }

    /**
     * MongoTemplate bean for MongoDB operations.
     * @param mongoClient MongoClient instance
     * @return MongoTemplate
     */
    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(new SimpleMongoClientDatabaseFactory(mongoClient, databaseName));
    }
}
