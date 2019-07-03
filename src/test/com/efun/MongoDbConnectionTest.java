package com.efun;

import com.efun.model.RandomNumberResult;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.client.*;
import com.mongodb.MongoClientSettings;
import org.bson.Document;
import org.bson.types.ObjectId;

// class of testing connection to mongo with sample data
public class MongoDbConnectionTest {
    public static void main(String[] args) {

        ConnectionString connString = new ConnectionString(
                "mongodb+srv://msiwiak:test@cluster0-xk3sv.mongodb.net/test?retryWrites=true&w=majority");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connString)
                .retryWrites(true)
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase("sample_airbnb");

        MongoCollection<Document> collection  = database.getCollection("listingsAndReviews");

        MongoCursor<Document> cursor = collection.find().iterator();
      /*  try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
        } finally {
            cursor.close();
        }*/


        System.out.println(collection.countDocuments());

    }
}