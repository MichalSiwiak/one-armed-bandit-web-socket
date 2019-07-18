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

    public WinCheckerServiceImpl(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    @Override
    public boolean isWin(List<List<Integer>> symbols, List<List<Integer>> win) {

        win= gameConfig.getWins();
        List<List<Integer>> winSublist = new ArrayList<>();
        for (List<Integer> list : symbols) {
            winSublist.add(list.subList(0, 3));
        }

        Map<Integer, List<Integer>> positionsOfWins = getPositionsOfWins(win);

        for (Integer integer : positionsOfWins.keySet()) {
            List<Integer> list = positionsOfWins.get(integer);

            List<Integer> elementsToCompare = new ArrayList<>();
            for (Integer element : list) {
                elementsToCompare.add(getPositionsOfSymbols().get(element));
            }
            System.out.println("Elements to Compare " + elementsToCompare);
            boolean checkWin = compareEqualityOfNumbers(elementsToCompare);
            System.out.println(checkWin);
        }


        return false;
    }

    private Map<Integer, List<Integer>> getPositionsOfWins(List<List<Integer>> win) {
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int j = 0; j < win.size(); j++) {
            List<Integer> positions = new ArrayList<>();
            for (int i = 0; i < win.get(j).size(); i++) {
                if (win.get(j).get(i) == 1)
                    positions.add(i);
            }
            map.put(j, positions);
        }
        return map;
    }

    private List<Integer> getPositionsOfSymbols(List<List<Integer>> symbols) {
        List<Integer> positions = new ArrayList<>();
        for (int j = 0; j < symbols.size(); j++) {
            for (int i = 0; i < symbols.get(j).size(); i++) {
                positions.add(symbols.get(j).get(i));
            }
        }
        return positions;
    }

    private static boolean compareEqualityOfNumbers(List<Integer> numbers) {
        for (int i = 0; i < numbers.size() - 1; i++) {
            int check = (numbers.get(0) ^ numbers.get(i + 1));
            if (check != 0) {
                return false;
            }
        }
        return true;
    }
}
