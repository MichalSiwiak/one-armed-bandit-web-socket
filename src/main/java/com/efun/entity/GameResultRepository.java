package com.efun.entity;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GameResultRepository extends MongoRepository<GameResult, String> {

    public List<GameResult> findAll();



}