package com.efun;


import com.efun.components.ResultWin;
import com.efun.config.GameConfig;
import com.efun.message.Message;
import com.efun.message.SpinParams;
import com.efun.service.CheckResultService;
import com.efun.service.MessageProviderService;
import com.efun.service.RnoInformationService;
import com.efun.service.WinCheckerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BalanceTest {


    @Autowired
    private MessageProviderService messageProviderService;

    @Autowired
    GameConfig gameConfig;

    @Autowired
    WinCheckerService winCheckerService;


    @Test
    public void calculationOfBalanceTestForLotOfSpins() {

        List<Integer> activeReels = Arrays.asList(0, 1, 2);
        List<Integer> activeWinLines = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
        Message messageStart = messageProviderService.startGame(activeWinLines, activeReels, "testId");

        SpinParams spinParams = new SpinParams();
        spinParams.setAuthorizationToken(messageStart.getAuthorizationToken());
        spinParams.setGameId("testId");
        String bet = "100";
        spinParams.setRno("50");
        spinParams.setBet(bet);
        boolean win;

        BigDecimal balanceInit = new BigDecimal("5000");

        for (int i = 1; i <= 5000; i++) {

            spinParams.setRno(String.valueOf(i));
            Message messageSpin = messageProviderService.executeSpin(spinParams);
            BigDecimal balance = balanceInit.subtract(new BigDecimal(bet));
            win = winCheckerService.isWin(messageSpin.getSymbols(), activeReels, activeWinLines);


            if (win) {
                List<ResultWin> resultWinList = winCheckerService
                        .getWins(messageSpin.getSymbols(), activeReels, activeWinLines)
                        .getResultWinList();

                BigDecimal winValue = new BigDecimal("0");
                for (ResultWin resultWin : resultWinList) {
                    BigDecimal bigDecimal = new BigDecimal(String.valueOf(gameConfig.getWinnings()
                            .get(resultWin.getIndex())));
                    winValue = winValue.add(new BigDecimal(spinParams.getBet()).
                            multiply(bigDecimal));
                }

                balance = balance.add(winValue);
            }
            assertEquals(balance, messageSpin.getBalance());
            balanceInit = balance;
        }
    }
}
