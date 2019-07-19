package com.efun.service;

import com.efun.config.GameConfig;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WinCheckerServiceImpl implements WinCheckerService {


    private GameConfig gameConfig;
    private BigDecimal winInSpin;
    private List<Integer> winsArray = new ArrayList<>();

    public WinCheckerServiceImpl(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }


    @Override
    public boolean isWin(List<List<Integer>> reels, List<List<Integer>> wins) {

        boolean win = false;
        winsArray.clear();

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
    public List<Integer> getWinArray() {
        return winsArray;
    }

    @Override
    public BigDecimal getWinInSpin() {
        return winInSpin;
    }


    private BigDecimal calculateSumOfWins(List<Integer> winsArray) {
        BigDecimal sum = new BigDecimal("0");
        for (Integer integer : winsArray) {
            //sum = sum + gameConfig.getWinnings().get(integer);
            sum = sum.add(new BigDecimal(String.valueOf(gameConfig.getWinnings().get(integer))));
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

    /**
     * Method checking equality of different elements using XOR logical operator
     *
     * @param int[] numbers - list of numbers to check its equality
     * @return true when all elements equal and false when not
     * @author Michał Siwiak
     */
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


