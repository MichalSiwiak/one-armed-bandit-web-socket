package com.efun.service;

import com.efun.entity.CombinationResult;

import java.util.List;
import java.util.Map;

public interface CombinationService {

    public Map<String, List<Integer>> generateCombinationReels();
    public String getNameOfCombinationReels(List<Integer> activeReels);
    public List<CombinationResult> getTotalPossibleWinnings(String combinationId, List<Integer> activeWinLines);
    public CombinationResult getRnoPossibleWinnings(String combinationId, int rno);
}
