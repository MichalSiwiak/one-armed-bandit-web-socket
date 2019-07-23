package com.efun.service;

import java.util.List;

public interface RnoInformationService {

    public int calculateSpinPositions(int rno, int gameConfigSpin, int listSize);
    public List<Integer> getMovedList(List<Integer> numbers, int positions);
    public int calculateCyclicalPositionOfReels(List<Integer> activeReels);
}