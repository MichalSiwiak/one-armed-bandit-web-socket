package com.efun.service;

import com.efun.config.GameConfig;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class RnoInformationServiceImpl implements RnoInformationService {

    private GameConfig gameConfig;

    public RnoInformationServiceImpl(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

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

    @Override
    public int calculateCyclicalPositionOfReels(List<Integer> activeReels) {

        int multiply = 1;
        for (Integer activeReel : activeReels) {

            int i = 0;
            int count = 0;
            int size = gameConfig.getReels().get(activeReel).size();
            int spin = gameConfig.getSpin().get(activeReel);
            while (true) {
                int index = calculateSpinPositions(i + 1, spin, size);
                i++;
                count++;
                if (index == 0)
                    break;
            }
            multiply = multiply * count;
        }
        return multiply;
    }
}