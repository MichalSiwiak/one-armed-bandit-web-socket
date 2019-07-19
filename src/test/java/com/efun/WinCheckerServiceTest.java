package com.efun;


import com.efun.service.WinCheckerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

        List<List<Integer>> testReelsList1 = new ArrayList<>();
        testReelsList1.add(Arrays.asList(1, 1, 1, 3, 4, 4, 4, 5, 6, 7, 7, 0, 0, 2, 2, 3, 1, 1, 1, 2, 2));
        testReelsList1.add(Arrays.asList(7, 1, 7, 6, 6, 6, 5, 5, 5, 4, 3, 2, 2, 2, 1, 1, 1, 0, 0, 0, 2, 3, 4, 7));
        testReelsList1.add(Arrays.asList(0, 1, 0, 2, 0, 3, 4, 5, 2, 2, 6, 6, 6, 5, 5, 5, 2, 2, 1, 2, 3, 4, 1, 1, 1, 0, 7, 7, 5));

        List<List<Integer>> testWinsList1 = new ArrayList<>();
        testWinsList1.add(Arrays.asList(0, 1, 0, 0, 1, 0, 0, 1, 0)); // middle elements is equal
        testWinsList1.add(Arrays.asList(1, 1, 1, 0, 0, 0, 0, 0, 0)); // # left elements is equal

        boolean win1 = winCheckerService.isWin(testReelsList1, testWinsList1);

        assertEquals(win1, true);
        assertThat(winCheckerService.getWinInSpin(), equalTo(24.0));


        List<List<Integer>> testReelsList2 = new ArrayList<>();
        testReelsList2.add(Arrays.asList(1, 0, 1, 3, 4, 4, 4, 5, 6, 7, 7, 0, 0, 2, 2, 3, 1, 1, 1, 2, 2));
        testReelsList2.add(Arrays.asList(7, 1, 7, 6, 6, 6, 5, 5, 5, 4, 3, 2, 2, 2, 1, 1, 1, 0, 0, 0, 2, 3, 4, 7));
        testReelsList2.add(Arrays.asList(0, 1, 0, 2, 0, 3, 4, 5, 2, 2, 6, 6, 6, 5, 5, 5, 2, 2, 1, 2, 3, 4, 1, 1, 1, 0, 7, 7, 5));

        List<List<Integer>> testWinsList2 = new ArrayList<>();
        testWinsList2.add(Arrays.asList(0, 1, 0, 0, 1, 0, 0, 1, 0)); // middle elements is equal
        testWinsList2.add(Arrays.asList(1, 1, 1, 0, 0, 0, 0, 0, 0)); // # left elements is equal

        boolean win2 = winCheckerService.isWin(testReelsList2, testWinsList2);
        assertEquals(win2, false);
        assertThat(winCheckerService.getWinInSpin(), equalTo(0.0));

        List<List<Integer>> testReelsList3 = new ArrayList<>();
        testReelsList3.add(Arrays.asList(3, 1, 2, 4));
        testReelsList3.add(Arrays.asList(4, 5, 2, 7, 8, 4, 3, 2, 5));
        testReelsList3.add(Arrays.asList(3, 4, 2, 1, 1, 3, 4, 4, 0, 0, 0));

        List<List<Integer>> testWinsList3 = new ArrayList<>();
        testWinsList3.add(Arrays.asList(0, 0, 1, 0, 0, 1, 0, 0, 1)); //# top elements is equal

        boolean win3 = winCheckerService.isWin(testReelsList3, testWinsList3);
        assertEquals(win3, true);
        assertThat(winCheckerService.getWinInSpin(), equalTo(3.0));
    }
}
