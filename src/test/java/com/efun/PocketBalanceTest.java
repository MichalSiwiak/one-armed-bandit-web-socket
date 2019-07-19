package com.efun;


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
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PocketBalanceTest {


    @Autowired
    private MessageProviderService messageProviderService;

    @Autowired
    GameConfig gameConfig;

    @Autowired
    WinCheckerService winCheckerService;


    @Test
    public void startGameAndExecuteSpinTest() {


        List<Integer> activeReels = new ArrayList<>();
        activeReels.add(0);
        activeReels.add(1);
        activeReels.add(2);

        List<Integer> winLines = new ArrayList<>();
        Message messageStart = messageProviderService.startGame(winLines, activeReels, "testId");

        SpinParams spinParams = new SpinParams();
        spinParams.setAuthorizationToken(messageStart.getAuthorizationToken());
        spinParams.setGameId("testId");
        String bet = "100";
        spinParams.setRno("50");
        spinParams.setBet(bet);
        boolean win;

        BigDecimal balanceInit = new BigDecimal("5000");


        for (int i = 1; i <= 10000; i++) {

            spinParams.setRno(String.valueOf(i));
            Message messageSpin = messageProviderService.executeSpin(spinParams);
            BigDecimal balance = balanceInit.subtract(new BigDecimal(bet));
            win = winCheckerService.isWin(messageSpin.getSymbols(), gameConfig.getWins());

            if (win) {
                List<Integer> winArray = winCheckerService.getWinArray();
                BigDecimal winValue = new BigDecimal("0");
                for (Integer integer : winArray) {
                    BigDecimal bigDecimal = new BigDecimal(String.valueOf(gameConfig.getWinnings().get(integer)));
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