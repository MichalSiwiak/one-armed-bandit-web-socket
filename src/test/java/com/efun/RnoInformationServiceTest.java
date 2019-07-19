package com.efun;

import com.efun.config.GameConfig;
import com.efun.service.CheckResultService;
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
public class RnoInformationServiceTest {


    @Autowired
    RnoInformationService rnoInformationService;

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
        assertEquals(movedList, Arrays.asList(2, 3, 4, 5, 0,0, 1, 1, 5, 5, 7, 7, 8, 8, 9, 9, 0, 0, 0, 0, 0, 1));


    }
}