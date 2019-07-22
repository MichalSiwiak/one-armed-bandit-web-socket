package com.efun;

import com.efun.config.GameConfig;
import com.efun.message.Message;
import com.efun.message.SpinParams;
import com.efun.service.CheckResultService;
import com.efun.service.MessageProviderService;
import com.efun.service.RnoInformationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageProviderServiceTest {


    @Autowired
    private MessageProviderService messageProviderService;

    @Autowired
    private CheckResultService checkResultService;

    @Autowired
    RnoInformationService rnoInformationService;

    @Autowired
    GameConfig gameConfig;


    @Test
    public void startGameAndExecuteSpinTest() {

        List<Integer> activeReels = new ArrayList<>();
        activeReels.add(0);
        activeReels.add(1);
        activeReels.add(2);
        activeReels.add(3);

        List<Integer> winLines = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);

        Message messageStart = messageProviderService.startGame(winLines, activeReels, "testId");

        SpinParams spinParams = new SpinParams();
        spinParams.setAuthorizationToken(messageStart.getAuthorizationToken());
        spinParams.setGameId("testId");
        spinParams.setBet("50");
        spinParams.setRno("6000044"); // tested value - please type different rno int from 1 to some big value

        Message messageSpin;
        List<List<Integer>> symbols;
        List<List<Integer>> reelPositionInCache;

        List<Integer> list1;
        List<Integer> list2;
        List<Integer> list3;
        List<Integer> list4;
        List<List<Integer>> newSymbols;

        messageSpin = messageProviderService.executeSpin(spinParams);
        symbols = messageSpin.getSymbols();
        reelPositionInCache = checkResultService.getReelPositionFromCacheOrCalculateAndSave(activeReels, Integer.parseInt(spinParams.getRno()));
        assertEquals(symbols, checkResultService.getFirst3Symbols(reelPositionInCache));

        list1 = gameConfig.getReels().get(0);
        list2 = gameConfig.getReels().get(1);
        list3 = gameConfig.getReels().get(2);
        list4 = gameConfig.getReels().get(3);

        newSymbols = new ArrayList<>();
        newSymbols.add(rnoInformationService.getMovedList(list1, (gameConfig.getSpin().get(0) * Integer.parseInt(spinParams.getRno()))));
        newSymbols.add(rnoInformationService.getMovedList(list2, (gameConfig.getSpin().get(1) * Integer.parseInt(spinParams.getRno()))));
        newSymbols.add(rnoInformationService.getMovedList(list3, (gameConfig.getSpin().get(2) * Integer.parseInt(spinParams.getRno()))));
        newSymbols.add(rnoInformationService.getMovedList(list4, (gameConfig.getSpin().get(3) * Integer.parseInt(spinParams.getRno()))));

        assertEquals(symbols, checkResultService.getFirst3Symbols(newSymbols));

    }
}