package com.neu.prattle.db;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * this class provides the connection to the mongo server.
 */
public class DBConnection {
    private ConnectionString connectionString;

    /**
     * Class constructor; instantiates the database
     * connection with a predefined connection String
     */
    public DBConnection() {
        Properties properties = new Properties();
        try {
            properties.load(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("mongodb.properties")));
        } catch (IOException | NullPointerException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        String dbUrl = properties.getProperty("db.url");
        this.connectionString = new ConnectionString(dbUrl);
    }

    /**
     * MongoClient is the interface between our java program and MongoDB server; it is used
     * to create connection, connect to database, retrieve collection names and
     * create/read/update/delete database, collections, document etc.
     * this method provides an instance of the MongoDB client.
     *
     * @return an instance of the Mongo client
     */
    public MongoClient getConnection() {
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();

        return MongoClients.create(clientSettings);
    }
}
