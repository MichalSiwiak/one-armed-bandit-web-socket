package com.efun.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class RnoInformationServiceImpl implements RnoInformationService {

    @Override
    public int calculateSpinPositions(int rno, int gameConfigSpin, int listSize) {
        return (rno * gameConfigSpin) % listSize;
    }

    /**
     * Method moving list based on the given items
     *
     * @param List<Integer> numbers - list of numbers to move
     * @param int positions - number of sliding indexes
     * @return List<Integer> numbers - new list moved by number [positions]
     * @author Michał Siwiak
     */
    @Override
    public List<Integer> getMovedList(List<Integer> numbers, int positions) {
        List<Integer> moved = new ArrayList<>(numbers);
        Collections.rotate(moved, positions);
        return moved;
    }
}
