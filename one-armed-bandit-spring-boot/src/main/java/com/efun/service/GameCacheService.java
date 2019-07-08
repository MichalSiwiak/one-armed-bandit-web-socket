package com.efun.service;

import com.efun.model.RandomNumberResult;

import java.util.List;

public interface GameCacheService {

    public List<RandomNumberResult> findAll(String session);

    public List<RandomNumberResult> findWins(String session);

    public void save(RandomNumberResult randomNumberResult, String sessionId);

    public RandomNumberResult getOne(int rno, String session);

    public void removeData(String sessionId);
}
