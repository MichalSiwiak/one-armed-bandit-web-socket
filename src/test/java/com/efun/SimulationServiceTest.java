package com.efun;

import com.efun.components.SimulationReportEnd;
import com.efun.components.SimulationReportInit;
import com.efun.entity.CombinationResult;
import com.efun.message.InitParams;
import com.efun.message.Message;
import com.efun.message.SpinParams;
import com.efun.service.MessageProviderService;
import com.efun.service.SimulationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SimulationServiceTest {


    @Autowired
    private SimulationService simulationService;
    @Autowired
    private MessageProviderService messageProviderService;

    @Test
    public void generateLotOFSpinsTest() {

        List<Integer> activeReels = Arrays.asList(0, 1, 4);
        List<Integer> activeWinLines = Arrays.asList(7, 3, 6, 2, 1, 0);
        int start = 8496;
        int size = 500;

        SimulationReportInit simulationReportInit = new SimulationReportInit();
        simulationReportInit.setActiveReels(activeReels);
        simulationReportInit.setActiveWinLines(activeWinLines);
        simulationReportInit.setSize(size);
        simulationReportInit.setStart(start);
        simulationReportInit.setStartingBalance(new BigDecimal("50000"));
        simulationReportInit.setBet(new BigDecimal("100"));

        SimulationReportEnd report = simulationService
                .generateLotOFSpins(simulationReportInit);
        assertEquals(report.getCombinationResults().size(), size);

        //================================= checking equality from messageProviderService

        InitParams initParams = new InitParams();
        initParams.setReelsSelected(activeReels);
        initParams.setWinLinesSelected(activeWinLines);
        Message messageStart = messageProviderService.startGame(initParams, "5DB1A1C5B5F04A226780422E018219BE");
        SpinParams spinParams = new SpinParams();
        spinParams.setAuthorizationToken(messageStart.getAuthorizationToken());
        spinParams.setGameId("5DB1A1C5B5F04A226780422E018219BE");
        spinParams.setBet("50");

        for (CombinationResult combinationResult : report.getCombinationResults()) {
            int rno = combinationResult.getRno();
            spinParams.setRno(String.valueOf(rno));

            List<List<Integer>> symbols = messageProviderService.executeSpin(spinParams).getSymbols();
            List<List<Integer>> symbolsTested = combinationResult.getSymbols();

            assertEquals(symbols, symbolsTested);
            assertEquals(combinationResult.isWin(), messageProviderService.executeSpin(spinParams).isWin());
            assertEquals(combinationResult.getResultWinList(),
                    messageProviderService.executeSpin(spinParams).getTotalWinInSpin().getResultWinList());
        }
    }

    @Test
    public void generateLotOFSpinsTestTime() {
        List<Integer> activeReels = Arrays.asList(0, 4, 3);
        List<Integer> activeWinLines = Arrays.asList(7, 3, 1, 2, 4, 6);
        int start = 2000000;
        int size = 100000;

        SimulationReportInit simulationReportInit = new SimulationReportInit();
        simulationReportInit.setActiveReels(Arrays.asList(0, 1, 2));
        simulationReportInit.setActiveWinLines(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7));
        simulationReportInit.setSize(size);
        simulationReportInit.setStart(start);
        simulationReportInit.setStartingBalance(new BigDecimal("50000"));
        simulationReportInit.setBet(new BigDecimal("100"));

        SimulationReportEnd report = simulationService
                .generateLotOFSpins(simulationReportInit);

        assertEquals(report.getCombinationResults().size(), size);
    }

    public static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("Max must be greater than min");
        }
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }
}