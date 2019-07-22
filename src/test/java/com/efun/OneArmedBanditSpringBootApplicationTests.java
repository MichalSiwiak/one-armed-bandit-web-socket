package com.efun;

import com.efun.service.MessageProviderServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OneArmedBanditSpringBootApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void getRandomNumberInRangeTest() {

        Set<Integer> numbers = new HashSet<>();

        for (int i = 0; i < 1000; i++) {
            numbers.add(MessageProviderServiceImpl.getRandomNumberInRange(1, 3));
        }

        assertEquals(true, numbers.contains(1));
        assertEquals(true, numbers.contains(2));
        assertEquals(true, numbers.contains(3));
    }
}