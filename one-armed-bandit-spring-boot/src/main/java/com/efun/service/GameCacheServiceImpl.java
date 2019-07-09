package com.efun.service;

import com.efun.entity.RandomNumberResult;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameCacheServiceImpl implements GameCacheService {

    @Value("${connection_string_localhost}")
    private String connectionString;

    @Autowired
    private MongoDatabase mongoDataSource;

    @Override
    public List<RandomNumberResult> findAll(String sessionId) {
        return null;
    }

    @Override
    public List<RandomNumberResult> findWins(String sessionId) {
        MongoCollection<RandomNumberResult> collection = mongoDataSource
                .getCollection(sessionId, RandomNumberResult.class);
        FindIterable<RandomNumberResult> wins = collection
                .withDocumentClass(RandomNumberResult.class)
                .find(Filters.eq("win", true));

        List<RandomNumberResult> results = new ArrayList<>();

        for (RandomNumberResult win : wins) {
            results.add(win);
        }
        return results;
    }

    @Override
    public void save(RandomNumberResult randomNumberResult, String sessionId) {
        MongoCollection<RandomNumberResult> collection = mongoDataSource
                .getCollection(sessionId, RandomNumberResult.class);
        collection.insertOne(randomNumberResult);
    }

    @Override
    public RandomNumberResult getOne(int rno, String sessionId) {
        MongoCollection<RandomNumberResult> collection = mongoDataSource
                .getCollection(sessionId, RandomNumberResult.class);
        RandomNumberResult randomNumber = collection
                .withDocumentClass(RandomNumberResult.class)
                .find(Filters.eq("randomNumber", rno))
                .first();
        return randomNumber;
    }

    @Override
    public void removeData(String sessionId) {
        MongoCollection<RandomNumberResult> collection = mongoDataSource
                .getCollection(sessionId, RandomNumberResult.class);
        collection.drop();
    }

}
