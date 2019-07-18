package com.efun.test;

import com.efun.config.GameConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class TestClass {

   /* @Autowired
    GameConfig gameConfig;

    @PostConstruct
    void init() {
        Map<Integer, List<Integer>> positionsOfWins = getPositionsOfWins();
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
    }

    private Map<Integer, List<Integer>> getPositionsOfWins() {
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int j = 0; j < gameConfig.getWins().size(); j++) {
            List<Integer> positions = new ArrayList<>();
            for (int i = 0; i < gameConfig.getWins().get(j).size(); i++) {
                if (gameConfig.getWins().get(j).get(i) == 1)
                    positions.add(i);
            }
            map.put(j, positions);
        }
        return map;
    }

    private List<Integer> getPositionsOfSymbols() {
        List<Integer> positions = new ArrayList<>();
        for (int j = 0; j < gameConfig.getReels().size(); j++) {
            for (int i = 0; i < gameConfig.getReels().get(j).size(); i++) {
                positions.add(gameConfig.getReels().get(j).get(i));
            }
        }
        return positions;
    }

    public static boolean compareEqualityOfNumbers(List<Integer> numbers) {
        for (int i = 0; i < numbers.size() - 1; i++) {
            int check = (numbers.get(0) ^ numbers.get(i + 1));
            if (check != 0) {
                return false;
            }
        }
        return true;
    }*/
}