package com.efun.results;

import com.efun.config.GameConfig;
import com.efun.config.GameConfigSingletonBuilder;
import com.efun.database.DBConfig;
import com.efun.model.RandomNumberResult;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import java.util.*;
import java.util.stream.Collectors;

//class represents calculation of game results holding in java memory in HashMap
public class GameResultsCache {

    public static void main(String[] args) {

        MongoDatabase datasource = DBConfig.getDatasource();
        MongoCollection<RandomNumberResult> collection = datasource.getCollection("results", RandomNumberResult.class);

        GameConfig gameConfig = GameConfigSingletonBuilder.getInstance();
        List<List<Byte>> reels = gameConfig.getReels();

        /*
        parameters:
        winlines: number <3,12>,
        activeReels: number <3,5>

        //we have 5 reels from: 0,1,2,3,4
        List<Integer> winlines = Arrays.asList(3, 4, 5, 6, 7);
 */
        //int activeReelsArray[] = {2, 3, 4};
        int activeReelsArray[] = {0, 1, 2};
        List<Integer> activeReels = Arrays.stream(activeReelsArray).boxed().collect(Collectors.toList());

        //what is the maximum of rno number ??
        //saving object to database
        for (int i = 1; i <= 500; i++) {

            RandomNumberResult randomNumberResult = new RandomNumberResult();
            randomNumberResult.setRandomNumber(i);
            List<List<Byte>> reelsInRandomNumber = new ArrayList<>();

            for (Integer activeReel : activeReels) {
                Collections.rotate(reels.get(activeReel), gameConfig.getSpin().get(activeReel));
                List<Byte> reel = new ArrayList<>(reels.get(activeReel).subList(0, 3));
                reelsInRandomNumber.add(reel);
            }

            //checking if is win
            if ((reelsInRandomNumber.get(0).get(1).equals(reelsInRandomNumber.get(1).get(1)))
                    && (reelsInRandomNumber.get(0).get(1).equals(reelsInRandomNumber.get(2).get(1)))) {
                randomNumberResult.setWin(true);
            }

            randomNumberResult.setReelsInRandomNumber(reelsInRandomNumber);
            collection.insertOne(randomNumberResult);
        }

        //get one example element
        RandomNumberResult randomNumber = collection
                .withDocumentClass(RandomNumberResult.class)
                .find(Filters.eq("randomNumber", 233))
                .first();

        System.out.println(randomNumber.toString());
        System.out.println("=====================");

        //get only wins elements
        FindIterable<RandomNumberResult> wins = collection
                .withDocumentClass(RandomNumberResult.class)
                .find(Filters.eq("win", true));

        for (RandomNumberResult win : wins) {
            System.out.println(win.toString());
        }

        //collection.drop();
    }
}