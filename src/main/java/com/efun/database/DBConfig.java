package com.efun.database;

import com.efun.utils.Utils;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class DBConfig {

    private static final String connectionString = Utils.readDatabaseSettings().get("connection_string_localhost");

    public static MongoDatabase getDatasource(){
        ConnectionString connString = new ConnectionString(connectionString);

        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connString)
                .retryWrites(true)
                .codecRegistry(pojoCodecRegistry)
                .build();

        com.mongodb.client.MongoClient mongoClient = MongoClients.create(settings);

        MongoDatabase datasource = mongoClient.getDatabase("games")
                .withCodecRegistry(pojoCodecRegistry);

        return datasource;
    }
}
