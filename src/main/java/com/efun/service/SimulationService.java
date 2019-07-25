package com.efun.service;

import com.efun.entity.CombinationResult;

import java.util.List;

public interface SimulationService {

    public List<CombinationResult> generateLotOFSpins(int start, int end, int size, List<Integer> activeReels, List<Integer> activeWinLines);
}
