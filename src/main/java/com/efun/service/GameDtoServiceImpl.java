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

    @Value("${collection_results_of_games}")
    private String collectionResults;

    private MongoDatabase mongoDataSource;

    public GameDtoServiceImpl(MongoDatabase mongoDataSource) {
        this.mongoDataSource = mongoDataSource;
    }

    /**
     * Method returns total list of games
     * saved in Mongo database
     * using pojo object GameDto
     *
     * @author Michał Siwiak
     * @return List<GameDto> list of total games
     *
     */

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

    /**
     * Method saving and editing one game information
     * when game is creating, updating or closing
     * using pojo object GameDto
     *
     * @author Michał Siwiak
     * @param GameDto gameDto - game which is updated
     *
     */
    @Override
    public void save(GameDto gameDto) {
        MongoCollection<GameDto> collection = mongoDataSource
                .getCollection(collectionResults, GameDto.class);
        collection.insertOne(gameDto);
    }

    /**
     * Method getting one game information based on gameID
     * using pojo object GameDto
     *
     * @author Michał Siwiak
     * @param String sessionId - generated sessionId of the game using md5
     * @return GameDto gameDto
     *
     */
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

    /**
     * Method updating one game information based on gameID
     * using pojo object GameDto
     *
     * @author Michał Siwiak
     * @param String sessionId - generated sessionId of the game using md5
     * @param Document document - editing object using class Document
     *
     */
    @Override
    public void updateOne(String gameId, Document document) {
        Bson filter = Filters.eq("gameId", gameId);
        MongoCollection<GameDto> collection = mongoDataSource
                .getCollection(collectionResults, GameDto.class);
        collection.updateOne(filter, new Document("$set", document));
    }
}
