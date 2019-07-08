package com.efun.model;

import com.efun.config.GameConfig;
import com.efun.service.GameCacheService;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
//Temporary class for tests - will moved to tests
public class RunAtStart {

    @Autowired
    private GameConfig gameConfig;

    @Autowired
    private GameCacheService gameCacheService;



    @PostConstruct
    public void runAtStart() {


        //MongoDatabase mongoDatabase = mongoClient.getDatabase("games");
        //MongoCollection<RandomNumberResult> collection = mongoDataSource.getCollection("results", RandomNumberResult.class);
        List<List<Byte>> reels = gameConfig.getReels();

        /*
        parameters:
        winlines: number <3,12>,
        activeReels: number <3,5>

        //we have 5 reels from: 0,1,2,3,4
        List<Integer> winlines = Arrays.asList(3, 4, 5, 6, 7);
 */
        int activeReelsArray[] = {2, 3, 4};
        //int activeReelsArray[] = {0, 1, 2};
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
            gameCacheService.save(randomNumberResult,"dgteia");
            System.out.println(randomNumberResult.toString());
        }
        System.out.println("=====================");
        //get one example element

        RandomNumberResult one = gameCacheService.getOne(88,"dgteia");
        System.out.println(one.toString());


        System.out.println("=====================");
        //get only wins elements
        List<RandomNumberResult> dgteia = gameCacheService.findWins("dgteia");
        for (RandomNumberResult randomNumberResult : dgteia) {
            System.out.println(randomNumberResult);
        }
        System.out.println("=====================");
        gameCacheService.removeData("dgteia");
        System.out.println("data removed");



    }
}
