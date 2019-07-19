package com.efun;

import com.efun.service.ReelsNamesService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReelsNamesServiceTest {


    @Autowired
    ReelsNamesService reelsNamesService;


    @Test
    public void getNameOfReelTest() {

        List<Integer> list;
        String nameOfReel;

        list = Arrays.asList(0, 1, 0, 0, 1, 0, 0, 1, 0);
        nameOfReel = reelsNamesService.getNameOfReel(list, 9);
        assertEquals(nameOfReel, "18981530CCF92B8AC21B81DA6FDD84F1");

        list = Arrays.asList(1,2,3,4);
        nameOfReel = reelsNamesService.getNameOfReel(list, 66);
        assertEquals(nameOfReel, "247F0C5804188FBCBED0E6A140AABD45");

        list = Arrays.asList(5,5,5);
        nameOfReel = reelsNamesService.getNameOfReel(list, 9);
        assertEquals(nameOfReel, "5DE8A36008B04A6167761FA19B61AA6C");


    }
}