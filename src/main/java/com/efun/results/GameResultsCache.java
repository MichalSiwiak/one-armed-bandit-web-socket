package com.efun.results;

import com.efun.config.GameConfig;
import com.efun.model.RandomNumberResult;
import com.google.gson.Gson;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

//class represents calculation of game results holding in java memory in HashMap
public class GameResultsCache {

    public static void main(String[] args) throws FileNotFoundException {

        Gson gson = new Gson();
        File gameConfigFile = ResourceUtils.getFile("classpath:config.json");
        Reader reader = new FileReader(gameConfigFile);
        GameConfig gameConfig = gson.fromJson(reader, GameConfig.class);
        List<List<Byte>> reels = gameConfig.getReels();

        /*
        parameters:
        winlines: number <3,12>,
        activeReels: number <3,5>
         */
        //we have 5 reels from: 0,1,2,3,4

        List<Integer> winlines = Arrays.asList(3, 4, 5, 6, 7);

        int activeReelsArray[] = {2, 3, 4};
        List<Integer> activeReels = Arrays.stream(activeReelsArray).boxed().collect(Collectors.toList());
        Map<Integer, RandomNumberResult> gameResultsCache = new HashMap<>();

        //what is the maximum of rno number ??
        for (int i = 1; i <= 10000; i++) {

            RandomNumberResult randomNumberResult = new RandomNumberResult();
            randomNumberResult.setRandomNumber(i);
            List<List<Byte>> reelsInRandomNumber = new ArrayList<>();

            for (Integer activeReel : activeReels) {
                Collections.rotate(reels.get(activeReel), gameConfig.getSpin().get(activeReel));
                List<Byte> reel = new ArrayList<>(reels.get(activeReel).subList(0,3));
                reelsInRandomNumber.add(reel);
            }

            randomNumberResult.setReelsInRandomNumber(reelsInRandomNumber);
            gameResultsCache.put(i, randomNumberResult);
        }

        for (Integer rno : gameResultsCache.keySet()) {
            System.out.println(gameResultsCache.get(rno));
        }
    }
}