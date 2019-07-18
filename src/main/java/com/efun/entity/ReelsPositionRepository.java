package com.efun.entity;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface ReelsPositionRepository extends MongoRepository<ReelPosition, String> {

    ReelPosition findByPositionsAndMd5Hex( int positions, String md5Hex);

}