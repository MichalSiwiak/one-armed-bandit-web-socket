package com.efun.service;

import com.efun.config.GameConfig;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WinCheckerServiceImpl implements WinCheckerService {


    private GameConfig gameConfig;
    private double winInSpin;

    public WinCheckerServiceImpl(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }


    @Override
    public boolean isWin(List<List<Integer>> reels, List<List<Integer>> wins) {

        boolean win = false;
        List<Integer> winsArray = new ArrayList<>();

        Map<Integer, List<Integer>> positionsOfWins = getPositionsOfWins(wins);
        for (Integer integer : positionsOfWins.keySet()) {

            List<Integer> list = positionsOfWins.get(integer);
            List<Integer> elementsToCompare = new ArrayList<>();

            for (Integer element : list) {
                elementsToCompare.add(getPositionsOfSymbols(reels).get(element));
            }
            boolean checkWin = compareEqualityOfNumbers(elementsToCompare);

            if (checkWin) {
                winsArray.add(elementsToCompare.iterator().next());
            }

            calculateSumOfWins(winsArray);
            win = win || checkWin;
        }
        return win;

    }

    @Override
    public double getWinInSpin() {
        return winInSpin;
    }

    private double calculateSumOfWins(List<Integer> winsArray) {
        double sum = 0;
        for (Integer integer : winsArray) {
            sum = sum + gameConfig.getWinnings().get(integer);
        }

        winInSpin = sum;
        return sum;
    }


    private Map<Integer, List<Integer>> getPositionsOfWins(List<List<Integer>> wins) {
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int j = 0; j < wins.size(); j++) {
            List<Integer> positions = new ArrayList<>();
            for (int i = 0; i < wins.get(j).size(); i++) {
                if (wins.get(j).get(i) == 1)
                    positions.add(i);
            }
            map.put(j, positions);
        }
        return map;
    }


    private List<Integer> getPositionsOfSymbols(List<List<Integer>> reels) {
        List<Integer> positions = new ArrayList<>();
        for (int j = 0; j < reels.size(); j++) {
            for (int i = 0; i < 3; i++) {
                positions.add(reels.get(j).get(i));
            }
        }
        return positions;
    }

    private boolean compareEqualityOfNumbers(List<Integer> numbers) {
        for (int i = 0; i < numbers.size() - 1; i++) {
            int check = (numbers.get(0) ^ numbers.get(i + 1));
            if (check != 0) {
                return false;
            }
        }
        return true;
    }
}


