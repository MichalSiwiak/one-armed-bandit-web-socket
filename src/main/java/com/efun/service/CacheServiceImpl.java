package com.efun.service;

import com.efun.entity.ReelPosition;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;


@Service
public class CacheServiceImpl implements CacheService {

    private MongoTemplate mongoTemplate;

    public CacheServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void save(ReelPosition reelPosition) {
        mongoTemplate.insert(reelPosition);
    }

}