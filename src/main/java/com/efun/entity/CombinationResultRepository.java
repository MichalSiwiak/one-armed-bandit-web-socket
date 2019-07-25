package com.efun.entity;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CombinationResultRepository extends MongoRepository<CombinationResult, String> {

    CombinationResult findByCombinationIdAndRno(String combinationId, int rno);

    List<CombinationResult> findByRnoIsInAndCombinationIdEquals(List<Integer> rno,String combinationId );

    List<CombinationResult> findByCombinationIdAndNumbersOfWinsContains(String combinationId, List<Integer> numbersOfWins);
}