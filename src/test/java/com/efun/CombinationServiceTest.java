package com.efun;

import com.efun.entity.CombinationResult;
import com.efun.message.InitParams;
import com.efun.message.Message;
import com.efun.message.SpinParams;
import com.efun.service.CombinationService;
import com.efun.service.MessageProviderService;
import com.efun.service.RnoInformationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CombinationServiceTest {


    @Autowired
    RnoInformationService rnoInformationService;
    @Autowired
    MessageProviderService messageProviderService;
    @Autowired
    CombinationService combinationService;

    @Test
    public void calculateCyclicalPositionOfReels() {

        List<List<Integer>> symbols;
        List<List<Integer>> symbolsChecked;

        int rno;

        //testing all combinations
        Map<String, List<Integer>> stringListMap = combinationService.generateCombinationReels();
        for (String key : stringListMap.keySet()) {

            int cyclicalPositionOfReels = rnoInformationService.calculateCyclicalPositionOfReels(stringListMap.get(key));
            InitParams initParams = new InitParams();
            initParams.setReelsSelected(stringListMap.get(key));
            //initParams.setWinLinesSelected(Arrays.asList(7,6));
            initParams.setWinLinesSelected(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7));
            Message messageStart = messageProviderService.startGame(initParams, "5DB1A1C5B5F04A226780422E018219BE");
            SpinParams spinParams = new SpinParams();
            spinParams.setBet("50"); //whatever more than 0;
            spinParams.setAuthorizationToken(messageStart.getAuthorizationToken());
            spinParams.setGameId("5DB1A1C5B5F04A226780422E018219BE");

            for (int i = 0; i <= 5; i++) {
                rno = getRandomNumberInRange(4575896, Integer.MAX_VALUE); // tested value - please type different rno int from 1 to some big value
                rno = rno % cyclicalPositionOfReels;
                spinParams.setRno(String.valueOf(rno));
                symbolsChecked = messageProviderService.executeSpin(spinParams).getSymbols();
                symbols = combinationService.getRnoPossibleWinnings(key, rno).getSymbols();
                assertEquals(symbols, symbolsChecked);
                assertEquals(combinationService.getRnoPossibleWinnings(key, rno).isWin(),
                        messageProviderService.executeSpin(spinParams).isWin());
                assertEquals(combinationService.getRnoPossibleWinnings(key, rno).getResultWinList(),
                        messageProviderService.executeSpin(spinParams).getTotalWinInSpin().getResultWinList());
            }
        }
    }

    public static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("Max must be greater than min");
        }
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }
}