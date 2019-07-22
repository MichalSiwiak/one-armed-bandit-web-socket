package com.efun;


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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WinCheckerServiceTest {

    @Autowired
    WinCheckerService winCheckerService;

    @Test
    public void isWinTestAndGetWinInSpinTest() {

        List<List<Integer>> symbols;
        List<Integer> activeReels;
        List<Integer> activeWinLines;

        activeReels = Arrays.asList(0, 1, 2);
        activeWinLines = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
        symbols = new ArrayList<>();
        symbols.add(Arrays.asList(4, 5, 6));
        symbols.add(Arrays.asList(7, 6, 6));
        symbols.add(Arrays.asList(2, 2, 6));

        assertEquals(winCheckerService.isWin(symbols, activeReels, activeWinLines), true);
        assertThat(winCheckerService.getWins(symbols, activeReels, activeWinLines)
                .getTotalMultiply().doubleValue(), equalTo(99.0));

        activeReels = Arrays.asList(0, 1, 2, 3, 4, 5);
        activeWinLines = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
        symbols = new ArrayList<>();
        symbols.add(Arrays.asList(4, 5, 7));
        symbols.add(Arrays.asList(7, 6, 7));
        symbols.add(Arrays.asList(2, 2, 7));
        symbols.add(Arrays.asList(5, 1, 7));
        symbols.add(Arrays.asList(5, 1, 7));

        assertEquals(winCheckerService.isWin(symbols, activeReels, activeWinLines), true);
        assertThat(winCheckerService.getWins(symbols, activeReels, activeWinLines)
                .getTotalMultiply().doubleValue(), equalTo(30.0));

        activeReels = Arrays.asList(0, 1, 2, 3, 4, 5);
        activeWinLines = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
        symbols = new ArrayList<>();
        symbols.add(Arrays.asList(4, 5, 7));
        symbols.add(Arrays.asList(7, 6, 7));
        symbols.add(Arrays.asList(2, 2, 7));
        symbols.add(Arrays.asList(5, 1, 7));
        symbols.add(Arrays.asList(5, 1, 4));


        assertEquals(winCheckerService.isWin(symbols, activeReels, activeWinLines), true);
        assertThat(winCheckerService.getWins(symbols, activeReels, activeWinLines)
                .getTotalMultiply().doubleValue(), equalTo(20.0));

        activeReels = Arrays.asList(0, 1, 2, 3, 4, 5);
        activeWinLines = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
        symbols = new ArrayList<>();
        symbols.add(Arrays.asList(4, 4, 7));
        symbols.add(Arrays.asList(7, 4, 4));
        symbols.add(Arrays.asList(2, 1, 7));
        symbols.add(Arrays.asList(5, 4, 7));
        symbols.add(Arrays.asList(5, 4, 4));


        assertEquals(winCheckerService.isWin(symbols, activeReels, activeWinLines), false);


        activeReels = Arrays.asList(0, 1, 2, 3, 4, 5);
        activeWinLines = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
        symbols = new ArrayList<>();
        symbols.add(Arrays.asList(4, 4, 7));
        symbols.add(Arrays.asList(7, 4, 7));
        symbols.add(Arrays.asList(2, 4, 7));
        symbols.add(Arrays.asList(5, 4, 7));
        symbols.add(Arrays.asList(5, 4, 4));


        assertEquals(winCheckerService.isWin(symbols, activeReels, activeWinLines), true);
        assertThat(winCheckerService.getWins(symbols, activeReels, activeWinLines)
                .getTotalMultiply().doubleValue(), equalTo(21.2));

        activeReels = Arrays.asList(0, 1, 2, 3, 4, 5);
        activeWinLines = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
        symbols = new ArrayList<>();
        symbols.add(Arrays.asList(4, 4, 7));
        symbols.add(Arrays.asList(7, 4, 7));
        symbols.add(Arrays.asList(2, 4, 7));
        symbols.add(Arrays.asList(5, 4, 3));
        symbols.add(Arrays.asList(5, 4, 4));


        assertEquals(winCheckerService.isWin(symbols, activeReels, activeWinLines), true);
        assertThat(winCheckerService.getWins(symbols, activeReels, activeWinLines)
                .getTotalMultiply().doubleValue(), equalTo(11.2));

        activeReels = Arrays.asList(0, 1, 2, 3, 4, 5);
        activeWinLines = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
        symbols = new ArrayList<>();
        symbols.add(Arrays.asList(5, 4, 7));
        symbols.add(Arrays.asList(5, 4, 7));
        symbols.add(Arrays.asList(5, 4, 7));
        symbols.add(Arrays.asList(5, 4, 3));
        symbols.add(Arrays.asList(5, 4, 4));


        assertEquals(winCheckerService.isWin(symbols, activeReels, activeWinLines), true);
        assertThat(winCheckerService.getWins(symbols, activeReels, activeWinLines)
                .getTotalMultiply().doubleValue(), equalTo(41.2));

    }
}
