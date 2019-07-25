package com.efun;

import com.efun.components.ResultWin;
import com.efun.components.TotalWinInSpin;
import com.efun.entity.CombinationResult;
import com.efun.entity.CombinationResultRepository;
import com.efun.message.EndParams;
import com.efun.message.InitParams;
import com.efun.message.Message;
import com.efun.message.SpinParams;
import com.efun.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CombinationResultRepositoryTest {


    @Autowired
    CombinationResultRepository combinationResultRepository;
    @Autowired
    RnoInformationService rnoInformationService;
    @Autowired
    CombinationService combinationService;
    @Autowired
    SimulationService simulationService;
    @Autowired
    MessageProviderService messageProviderService;
    @Autowired
    WinCheckerService winCheckerService;

    @Test
    public void findByCombinationIdAndRnoTest() {

        List<Integer> rno;
        rno = Arrays.asList(1167, 1177);
        String combinationId = "A9BD0EEB3CE6858DF275497BB2089EC4";
        List<CombinationResult> byCombinationIdAndRno =
                combinationResultRepository.findByRnoIsInAndCombinationIdEquals(rno, combinationId);
        assertEquals(byCombinationIdAndRno.size(), 2);

        //=================================

        List<Integer> activeReels;
        int periodicity;
        String nameOfCombinationReels;
        List<CombinationResult> total;
        List<List<Integer>> list;
        List<Integer> element;

        int start;
        int end;
        int size;
        int randomNumber;

        activeReels = Arrays.asList(0, 1, 2);
        periodicity = rnoInformationService.calculateCyclicalPositionOfReels(activeReels);
        nameOfCombinationReels = combinationService.getNameOfCombinationReels(activeReels);

        rno = Arrays.asList(11674534 % periodicity, 95478 % periodicity, 2548 % periodicity);

        List<CombinationResult> list1 =
                combinationResultRepository.findByRnoIsInAndCombinationIdEquals(rno, nameOfCombinationReels);

        assertEquals(list1.size(), 3);

        //=================================

        start = 1;
        end = 2000;
        size = 1000;
        randomNumber = getRandomNumberInRange(start, end);

        list = new ArrayList<>();
        element = new ArrayList<>();

        for (int i = randomNumber; i < randomNumber + size; i++) {
            if ((i % periodicity) == 0) {
                element.add(periodicity);
                list.add(element);
                element = new ArrayList<>();
            } else {
                element.add(i % periodicity);
            }
        }
        list.add(element);

        total = new ArrayList<>();
        for (List<Integer> integers : list) {
            total.addAll(combinationResultRepository.findByRnoIsInAndCombinationIdEquals(integers, nameOfCombinationReels));
        }

        assertEquals(size, total.size());

        //=================================

        activeReels = Arrays.asList(0, 1, 2, 4);
        periodicity = rnoInformationService.calculateCyclicalPositionOfReels(activeReels);
        nameOfCombinationReels = combinationService.getNameOfCombinationReels(activeReels);

        start = 1;
        end = 2000;
        size = 1000000;
        randomNumber = getRandomNumberInRange(start, end);

        list = new ArrayList<>();
        element = new ArrayList<>();

        for (int i = randomNumber; i < randomNumber + size; i++) {
            if ((i % periodicity) == 0) {
                element.add(periodicity);
                list.add(element);
                element = new ArrayList<>();
            } else {
                element.add(i % periodicity);
            }
        }
        list.add(element);

        total = new ArrayList<>();
        for (List<Integer> integers : list) {
            total.addAll(combinationResultRepository.findByRnoIsInAndCombinationIdEquals(integers, nameOfCombinationReels));
        }

        assertEquals(size, total.size());

        //=================================

        activeReels = Arrays.asList(0, 1, 2, 4, 3);
        periodicity = rnoInformationService.calculateCyclicalPositionOfReels(activeReels);
        nameOfCombinationReels = combinationService.getNameOfCombinationReels(activeReels);

        start = 1;
        end = 2000;
        size = 1000000;
        randomNumber = getRandomNumberInRange(start, end);

        list = new ArrayList<>();
        element = new ArrayList<>();

        for (int i = randomNumber; i < randomNumber + size; i++) {
            if ((i % periodicity) == 0) {
                element.add(periodicity);
                list.add(element);
                element = new ArrayList<>();
            } else {
                element.add(i % periodicity);
            }
        }
        list.add(element);

        total = new ArrayList<>();
        for (List<Integer> integers : list) {
            total.addAll(combinationResultRepository.findByRnoIsInAndCombinationIdEquals(integers, nameOfCombinationReels));
        }

        assertEquals(size, total.size());
    }

    @Test
    public void generateLotOFSpinsTest() {
        List<Integer> activeReels = Arrays.asList(0, 4, 3);
        List<Integer> activeWinLines = Arrays.asList(7, 3);
        int start = 500;
        int end = 1000000;
        int size = 1000;

        List<CombinationResult> combinationResults = simulationService
                .generateLotOFSpins(start, end, size, activeReels, activeWinLines);
        assertEquals(combinationResults.size(), size);

        //================================= checking equality from messageProviderService

        InitParams initParams = new InitParams();
        initParams.setReelsSelected(activeReels);
        initParams.setWinLinesSelected(activeWinLines);

        Message messageStart = messageProviderService.startGame(initParams, "5DB1A1C5B5F04A226780422E018219BE");

        SpinParams spinParams = new SpinParams();
        spinParams.setAuthorizationToken(messageStart.getAuthorizationToken());
        spinParams.setGameId("5DB1A1C5B5F04A226780422E018219BE");
        spinParams.setBet("50");

        for (CombinationResult combinationResult : combinationResults) {
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
        List<Integer> activeReels = Arrays.asList(0, 4, 3, 2, 1);
        List<Integer> activeWinLines = Arrays.asList(0, 7, 6, 5);
        int start = 500;
        int end = 1000000;
        int size = 10000;


        List<CombinationResult> combinationResults = simulationService
                .generateLotOFSpins(start, end, size, activeReels, activeWinLines);

    }

    public static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("Max must be greater than min");
        }
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }
}