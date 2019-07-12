package com.efun.service;

import com.efun.entity.GameDto;
import org.bson.Document;

import java.util.List;

public interface GameDtoService {

    public List<GameDto> findAll();

    public void save(GameDto gameDto);

    public GameDto getOne(String gameId);

    public void updateOne(String gameId, Document document);

}
