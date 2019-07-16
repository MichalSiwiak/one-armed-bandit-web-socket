package com.efun.service;

import com.efun.entity.GameDto;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class GameDtoServiceImpl implements GameDtoService {

    @Value("${connection_string_localhost}")
    private String connectionString;

    @Value("${collection_results_of_games}")
    private String collectionResults;

    @Autowired
    private MongoDatabase mongoDataSource;

    @Override
    public List<GameDto> findAll() {
        List<GameDto> gameDtoList = new ArrayList<>();
        MongoCollection<GameDto> collection = mongoDataSource
                .getCollection(collectionResults, GameDto.class);
        FindIterable<GameDto> cursor = collection.find();

        for (GameDto gameDto : cursor) {
            gameDtoList.add(gameDto);
        }
        return gameDtoList;
    }

    @Override
    public void save(GameDto gameDto) {
        MongoCollection<GameDto> collection = mongoDataSource
                .getCollection(collectionResults, GameDto.class);
        collection.insertOne(gameDto);
    }

    @Override
    public GameDto getOne(String gameId) {
        MongoCollection<GameDto> collection = mongoDataSource
                .getCollection(collectionResults, GameDto.class);
        GameDto gameDto = collection
                .withDocumentClass(GameDto.class)
                .find(Filters.eq("gameId", gameId))
                .first();
        return gameDto;
    }

    @Override
    public void updateOne(String gameId, Document document) {
        Bson filter = Filters.eq("gameId", gameId);
        MongoCollection<GameDto> collection = mongoDataSource
                .getCollection(collectionResults, GameDto.class);
        collection.updateOne(filter, new Document("$set", document));
    }
}
