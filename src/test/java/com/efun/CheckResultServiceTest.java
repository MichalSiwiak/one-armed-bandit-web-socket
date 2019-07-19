package com.efun;

import com.efun.config.GameConfig;
import com.efun.service.CheckResultService;
import com.efun.service.MessageProviderServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CheckResultServiceTest {


    @Autowired
    private CheckResultService checkResultService;

    @Autowired
    private GameConfig gameConfig;

    @Test
    public void isReelPositionInCacheTest() {

        List<Integer> activeReels = new ArrayList<>();
        activeReels.add(0);
        activeReels.add(1);
        activeReels.add(2);
        //activeReels.add(3);
        //activeReels.add(4);

        int rnoMax = 1000;

        for (int rno = 1; rno <= rnoMax; rno++) {

            List<List<Integer>> reelPositionInCache = checkResultService.isReelPositionInCache(activeReels, rno);
            for (int i = 0; i < reelPositionInCache.size(); i++) {
                List<Integer> listToCheck = reelPositionInCache.get(i);

                List<Integer> testList = gameConfig.getReels().get(i);
                Integer integer = gameConfig.getSpin().get(i);
                Collections.rotate(testList, integer * rno);
                assertEquals(listToCheck, testList);
            }
        }
    }
}