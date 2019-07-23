package com.efun.service;

import com.efun.entity.GameResult;
import java.util.List;

public interface GameResultService {

    public List<GameResult> findAll();

    public void save(GameResult gameDto);

    public GameResult getOne(String gameId);

    public void delete(String gameId);

}
