package com.efun;

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

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RnoInformationServiceTest {


    @Autowired
    RnoInformationService rnoInformationService;
    @Autowired
    MessageProviderService messageProviderService;
    @Autowired
    CombinationService combinationService;


    @Test
    public void calculateSpinPositionsTest() {

        int i;
        int result;

        i = rnoInformationService.calculateSpinPositions(77, 4, 11);
        result = (77 * 4) % 11;
        assertEquals(i, result);

        //third parameter could not be <=0 !!!
        i = rnoInformationService.calculateSpinPositions(5, 11, 88);
        result = (5 * 11) % 88;
        assertEquals(i, result);

        i = rnoInformationService.calculateSpinPositions(116, 66, 1001);
        result = (66 * 116) % 1001;
        assertEquals(i, result);
    }

    @Test
    public void getMovedListTest() {

        List<Integer> list;
        List<Integer> movedList;

        list = Arrays.asList(4, 6, 7, 8, 9, 5, 4, 5);
        movedList = rnoInformationService.getMovedList(list, 2);
        assertEquals(movedList, Arrays.asList(4, 5, 4, 6, 7, 8, 9, 5));

        list = Arrays.asList(0, 1, 1, 5, 5, 7, 7, 8, 8, 9, 9, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 0);
        movedList = rnoInformationService.getMovedList(list, 5);
        assertEquals(movedList, Arrays.asList(2, 3, 4, 5, 0, 0, 1, 1, 5, 5, 7, 7, 8, 8, 9, 9, 0, 0, 0, 0, 0, 1));
    }

    @Test
    public void calculateCyclicalPositionOfReels() {

        List<List<Integer>> symbols;
        List<List<Integer>> symbolsTested;
        int rno;

        //testing all combinations
        Map<String, List<Integer>> stringListMap = combinationService.generateCombinationReels();
        for (String key : stringListMap.keySet()) {

            int cyclicalPositionOfReels = rnoInformationService.calculateCyclicalPositionOfReels(stringListMap.get(key));
            InitParams initParams = new InitParams();
            initParams.setReelsSelected(stringListMap.get(key));
            Message messageStart = messageProviderService.startGame(initParams, "5DB1A1C5B5F04A226780422E018219BE");
            SpinParams spinParams = new SpinParams();
            spinParams.setBet("50"); //whatever more than 0;
            spinParams.setAuthorizationToken(messageStart.getAuthorizationToken());
            spinParams.setGameId("5DB1A1C5B5F04A226780422E018219BE");

            rno = 6000044; // tested value - please type different rno int from 1 to some big value
            spinParams.setRno(String.valueOf(rno));
            symbols = messageProviderService.executeSpin(spinParams).getSymbols();

            rno = rno % cyclicalPositionOfReels;
            spinParams.setRno(String.valueOf(rno));
            symbolsTested = messageProviderService.executeSpin(spinParams).getSymbols();

            assertEquals(symbols, symbolsTested);

            rno = 784596; // tested value - please type different rno int from 1 to some big value
            spinParams.setRno(String.valueOf(rno));
            symbols = messageProviderService.executeSpin(spinParams).getSymbols();

            rno = rno % cyclicalPositionOfReels;
            spinParams.setRno(String.valueOf(rno));
            symbolsTested = messageProviderService.executeSpin(spinParams).getSymbols();

            assertEquals(symbols, symbolsTested);

            rno = 415; // tested value - please type different rno int from 1 to some big value
            spinParams.setRno(String.valueOf(rno));
            symbols = messageProviderService.executeSpin(spinParams).getSymbols();

            rno = rno % cyclicalPositionOfReels;
            spinParams.setRno(String.valueOf(rno));
            symbolsTested = messageProviderService.executeSpin(spinParams).getSymbols();

            assertEquals(symbols, symbolsTested);


            rno = 874589632; // tested value - please type different rno int from 1 to some big value
            spinParams.setRno(String.valueOf(rno));
            symbols = messageProviderService.executeSpin(spinParams).getSymbols();

            rno = rno % cyclicalPositionOfReels;
            spinParams.setRno(String.valueOf(rno));
            symbolsTested = messageProviderService.executeSpin(spinParams).getSymbols();

            assertEquals(symbols, symbolsTested);

        }

    }

}