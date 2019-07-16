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

    /**
     * Method returning all spins from temporary table of created game
     * from mongo database
     *
     * @author Michał Siwiak
     * @param String sessionId - generated sessionId of the game using md5
     * @return List<RandomNumberResult> list of total spins
     *
     */

    @Override
    public List<RandomNumberResult> findAll(String sessionId) {
        MongoCollection<RandomNumberResult> collection = mongoDataSource
                .getCollection(sessionId, RandomNumberResult.class);
        FindIterable<RandomNumberResult> total = collection
                .withDocumentClass(RandomNumberResult.class)
                .find();

        List<RandomNumberResult> results = new ArrayList<>();

        for (RandomNumberResult randomNumberResult : total) {
            results.add(randomNumberResult);
        }
        return results;

    }

    /**
     * Method filtering only WIN spins from total spins using
     * temporary table of created game from mongo database
     *
     * @author Michał Siwiak
     * @param String sessionId - generated sessionId of the game using md5
     * @return List<RandomNumberResult> list of WIN spins
     *
     */

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

    /**
     * Method saving one spin before game is creating
     *
     * @author Michał Siwiak
     * @param RandomNumberResult randomNumberResult - saved spin
     * @param String sessionId - generated sessionId of the game using md5
     *
     */
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

    /**
     * Method removing all temporary data of spins when game is closing
     *
     * @author Michał Siwiak
     * @param String sessionId - generated sessionId of the game using md5
     *
     */
    @Override
    public void removeData(String sessionId) {
        MongoCollection<RandomNumberResult> collection = mongoDataSource
                .getCollection(sessionId, RandomNumberResult.class);
        collection.drop();
    }

}
