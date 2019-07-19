package com.efun.service;

import com.efun.entity.GameResult;
import com.efun.entity.GameResultRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GameResultServiceImpl implements GameResultService {

    private GameResultRepository gameResultRepository;
    private MongoTemplate mongoTemplate;

    public GameResultServiceImpl(GameResultRepository gameResultRepository,
                                 MongoTemplate mongoTemplate) {
        this.gameResultRepository = gameResultRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<GameResult> findAll() {
        return gameResultRepository.findAll();
    }

    @Override
    public void save(GameResult gameResult) {
        mongoTemplate.save(gameResult);
    }

    @Override
    public GameResult getOne(String gameId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("gameId").is(gameId));
        GameResult gameResult = mongoTemplate.findOne(query, GameResult.class);
        return gameResult;
    }
}
