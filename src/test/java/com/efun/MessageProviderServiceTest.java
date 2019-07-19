package com.efun;

import com.efun.config.GameConfig;
import com.efun.message.Message;
import com.efun.service.CheckResultService;
import com.efun.service.MessageProviderService;
import com.efun.service.RnoInformationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
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

        List<Integer> winLines = new ArrayList<>();

        Message messageStart = messageProviderService.startGame(winLines, activeReels, "testId");
        int rno = messageStart.getRno();
        String authorizationToken = messageStart.getAuthorizationToken();
        String gameId = messageStart.getGameId();

        Message messageSpin;
        List<List<Integer>> symbols;
        List<List<Integer>> reelPositionInCache;

        List<Integer> list1;
        List<Integer> list2;
        List<Integer> list3;
        List<List<Integer>> newSymbols;

        rno = rno + 1;
        messageSpin = messageProviderService.executeSpin(rno, 50, authorizationToken, gameId);
        symbols = messageSpin.getSymbols();
        reelPositionInCache = checkResultService.isReelPositionInCache(activeReels, rno);
        assertEquals(symbols, checkResultService.getFirst3Symbols(reelPositionInCache));

        list1 = gameConfig.getReels().get(0);
        list2 = gameConfig.getReels().get(1);
        list3 = gameConfig.getReels().get(2);

        newSymbols = new ArrayList<>();
        newSymbols.add(rnoInformationService.getMovedList(list1, (gameConfig.getSpin().get(0) * rno)));
        newSymbols.add(rnoInformationService.getMovedList(list2, (gameConfig.getSpin().get(1) * rno)));
        newSymbols.add(rnoInformationService.getMovedList(list3, (gameConfig.getSpin().get(2) * rno)));

        assertEquals(symbols, checkResultService.getFirst3Symbols(newSymbols));

        rno = rno + 1;
        messageSpin = messageProviderService.executeSpin(rno, 50, authorizationToken, gameId);
        symbols = messageSpin.getSymbols();
        reelPositionInCache = checkResultService.isReelPositionInCache(activeReels, rno);
        assertEquals(symbols, checkResultService.getFirst3Symbols(reelPositionInCache));

        list1 = gameConfig.getReels().get(0);
        list2 = gameConfig.getReels().get(1);
        list3 = gameConfig.getReels().get(2);

        newSymbols = new ArrayList<>();
        newSymbols.add(rnoInformationService.getMovedList(list1, (gameConfig.getSpin().get(0) * rno)));
        newSymbols.add(rnoInformationService.getMovedList(list2, (gameConfig.getSpin().get(1) * rno)));
        newSymbols.add(rnoInformationService.getMovedList(list3, (gameConfig.getSpin().get(2) * rno)));

        assertEquals(symbols, checkResultService.getFirst3Symbols(newSymbols));

        rno = rno + 1;
        messageSpin = messageProviderService.executeSpin(rno, 50, authorizationToken, gameId);
        symbols = messageSpin.getSymbols();
        reelPositionInCache = checkResultService.isReelPositionInCache(activeReels, rno);
        assertEquals(symbols, checkResultService.getFirst3Symbols(reelPositionInCache));

        list1 = gameConfig.getReels().get(0);
        list2 = gameConfig.getReels().get(1);
        list3 = gameConfig.getReels().get(2);

        newSymbols = new ArrayList<>();
        newSymbols.add(rnoInformationService.getMovedList(list1, (gameConfig.getSpin().get(0) * rno)));
        newSymbols.add(rnoInformationService.getMovedList(list2, (gameConfig.getSpin().get(1) * rno)));
        newSymbols.add(rnoInformationService.getMovedList(list3, (gameConfig.getSpin().get(2) * rno)));

        assertEquals(symbols, checkResultService.getFirst3Symbols(newSymbols));

        rno = rno + 1;
        messageSpin = messageProviderService.executeSpin(rno, 50, authorizationToken, gameId);
        symbols = messageSpin.getSymbols();
        reelPositionInCache = checkResultService.isReelPositionInCache(activeReels, rno);
        assertEquals(symbols, checkResultService.getFirst3Symbols(reelPositionInCache));

        list1 = gameConfig.getReels().get(0);
        list2 = gameConfig.getReels().get(1);
        list3 = gameConfig.getReels().get(2);

        newSymbols = new ArrayList<>();
        newSymbols.add(rnoInformationService.getMovedList(list1, (gameConfig.getSpin().get(0) * rno)));
        newSymbols.add(rnoInformationService.getMovedList(list2, (gameConfig.getSpin().get(1) * rno)));
        newSymbols.add(rnoInformationService.getMovedList(list3, (gameConfig.getSpin().get(2) * rno)));

        assertEquals(symbols, checkResultService.getFirst3Symbols(newSymbols));


    }
}