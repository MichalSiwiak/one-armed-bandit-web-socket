package com.efun;

import com.efun.message.EndParams;
import com.efun.message.InitParams;
import com.efun.message.SpinParams;
import com.efun.validation.ValidationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ValidationServiceTest {

    @Autowired
    ValidationService validationService;

    @Test
    public void validateSpinParamsTest() {
        SpinParams spinParams = null;
        assertEquals(false, validationService.validateSpinParams(spinParams));
        spinParams = new SpinParams();
        spinParams.setBet("76");
        spinParams.setRno("6");
        spinParams.setGameId("E511F38719441CB0C238E9F24727AC4C");
        spinParams.setAuthorizationToken("E511F38719441CB0C238E9F24727AC4C");
        assertEquals(true, validationService.validateSpinParams(spinParams));
        spinParams.setGameId("E511F38719441CB0C238E9F24727AC4Cs");
        spinParams.setAuthorizationToken("E511F38719441CB0C238E9F24727AC4C");
        assertEquals(false, validationService.validateSpinParams(spinParams));
        spinParams.setBet("7dfdf6");
        spinParams.setRno("6");
        spinParams.setGameId("E511F38719441CB0C238E9F24727AC4C");
        spinParams.setAuthorizationToken("E511F38719441CB0C238E9F24727AC4C");
        assertEquals(false, validationService.validateSpinParams(spinParams));
        spinParams.setBet("76");
        spinParams.setRno("-6");
        spinParams.setGameId("E511F38719441CB0C238E9F24727AC4C");
        spinParams.setAuthorizationToken("E511F38719441CB0C238E9F24727AC4C");
        assertEquals(false, validationService.validateSpinParams(spinParams));
        spinParams.setBet("0");
        spinParams.setRno("6");
        spinParams.setGameId("E511F38719441CB0C238E9F24727AC4C");
        spinParams.setAuthorizationToken("E511F38719441CB0C238E9F24727AC4C");
        assertEquals(false, validationService.validateSpinParams(spinParams));
        spinParams.setBet("1");
        spinParams.setRno("0");
        spinParams.setGameId("E511F38719441CB0C238E9F24727AC4C");
        spinParams.setAuthorizationToken("E511F38719441CB0C238E9F24727AC4C");
        assertEquals(false, validationService.validateSpinParams(spinParams));
        spinParams.setBet("1");
        spinParams.setRno("1");
        spinParams.setGameId("E511F38719441CB0C238E9F24727AC4C");
        spinParams.setAuthorizationToken("E511F38719441CB0C238E9F24727AC4C");
        assertEquals(true, validationService.validateSpinParams(spinParams));
    }

    @Test
    public void validateInitParamsTest() {

        List<Integer> activeWinLines;
        List<Integer> activeReels;
        String gameId;

        activeWinLines = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
        activeReels = Arrays.asList(0, 1, 2, 3, 4);
        InitParams initParams = new InitParams();
        initParams.setReelsSelected(activeReels);
        initParams.setWinLinesSelected(activeWinLines);
        gameId = "E511F38719441CB0C238E9F24727AC4C";
        assertEquals(true, validationService.validateInitParams(initParams, gameId));

        activeWinLines = null;
        activeReels = Arrays.asList(0, 1, 2, 3, 4);
        initParams.setReelsSelected(activeReels);
        initParams.setWinLinesSelected(activeWinLines);
        gameId = "E511F38719441CB0C238E9F24727AC4C";
        assertEquals(false, validationService.validateInitParams(initParams, gameId));

        activeWinLines = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
        activeReels = null;
        initParams.setReelsSelected(activeReels);
        initParams.setWinLinesSelected(activeWinLines);
        gameId = "E511F38719441CB0C238E9F24727AC4C";
        assertEquals(false, validationService.validateInitParams(initParams, gameId));

        activeWinLines = null;
        activeReels = null;
        initParams.setReelsSelected(activeReels);
        initParams.setWinLinesSelected(activeWinLines);
        gameId = "E511F38719441CB0C238E9F24727AC4C";
        assertEquals(false, validationService.validateInitParams(initParams, gameId));

        activeWinLines = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
        activeReels = Arrays.asList(0, 1, 2, 3, 4);
        initParams.setReelsSelected(activeReels);
        initParams.setWinLinesSelected(activeWinLines);
        gameId = "E511F38719441CB0C238E9F24727AC4Cd";
        assertEquals(true, validationService.validateInitParams(initParams, gameId));

        activeWinLines = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
        activeReels = Arrays.asList(0, 1, 2, 3, 4, 6);
        initParams.setReelsSelected(activeReels);
        initParams.setWinLinesSelected(activeWinLines);
        gameId = "E511F38719441CB0C238E9F24727AC4C";
        assertEquals(false, validationService.validateInitParams(initParams, gameId));

        activeWinLines = Arrays.asList(0, 1, 2, 3, 4, 5);
        activeReels = Arrays.asList(0, 1, 2, 3);
        initParams.setReelsSelected(activeReels);
        initParams.setWinLinesSelected(activeWinLines);
        gameId = "E511F38719441CB0C238E9F24727AC4C";
        assertEquals(true, validationService.validateInitParams(initParams, gameId));


        activeWinLines = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
        activeReels = Arrays.asList(0, 1, 9, 2, 3, 4);
        initParams.setReelsSelected(activeReels);
        initParams.setWinLinesSelected(activeWinLines);
        gameId = "E511F38719441CB0C238E9F24727AC4C";
        assertEquals(false, validationService.validateInitParams(initParams, gameId));

        activeWinLines = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8);
        activeReels = Arrays.asList(0, 1, 2, 3, 4);
        initParams.setReelsSelected(activeReels);
        initParams.setWinLinesSelected(activeWinLines);
        gameId = "E511F38719441CB0C238E9F24727AC4Cd";
        assertEquals(false, validationService.validateInitParams(initParams, gameId));

    }

    @Test
    public void validateEndParamsTest() {

        EndParams endParams= new EndParams();

        endParams.setAuthorizationToken("E511F38719441CB0C238E9F24727AC4C");
        endParams.setGameId("E511F38719441CB0C238E9F24727AC4C");
        assertEquals(true, validationService.validateEndParams(endParams));

        endParams.setAuthorizationToken("E511F38719441CBdd0C238E9F24727AC4C");
        endParams.setGameId("E511F38719441CB0C238E9F24727AC4C");
        assertEquals(false, validationService.validateEndParams(endParams));

        endParams.setAuthorizationToken("");
        endParams.setGameId("E511F38719441CB0C238E9F24727AC4C");
        assertEquals(false, validationService.validateEndParams(endParams));

        endParams.setAuthorizationToken("E511F38719441CB0C238E9F24727AC4C");
        endParams.setGameId("");
        assertEquals(false, validationService.validateEndParams(endParams));

        endParams.setAuthorizationToken("E511F38719441CB0C238E9F24727AC4C");
        endParams.setGameId(null);
        assertEquals(false, validationService.validateEndParams(endParams));

        endParams.setAuthorizationToken(null);
        endParams.setGameId(null);
        assertEquals(false, validationService.validateEndParams(endParams));
    }


}
