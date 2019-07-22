package com.efun;


import com.efun.config.GameConfig;
import com.efun.config.WinLine;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameConfigTest {

    @Autowired
    private GameConfig gameConfig;


    @Test
    public void correctnessOfConfigurationFileTest() {
        System.out.println(gameConfig.toString());
        //checking if config file is not null
        assertNotNull(gameConfig);
        System.out.println(ReflectionToStringBuilder.toString(gameConfig, ToStringStyle.MULTI_LINE_STYLE));

        //number of reels must equal to array size of spins
        assertEquals(gameConfig.getReels().size(), gameConfig.getSpin().size());

        //now we calculate unique ids from reels:
        Set<Integer> uniqueIds = new HashSet<>();
        List<List<Integer>> reels = gameConfig.getReels();

        for (List<Integer> reel : reels) {
            for (Integer id : reel) {
                uniqueIds.add(id);
            }
        }
        //and size of set of unique ids must equal to winnings array
        assertEquals(uniqueIds.size(), gameConfig.getWinnings().size());


        List<WinLine> winLines = gameConfig.getWinLines();
        for (WinLine winLine : winLines) {
            List<List<Integer>> positions = winLine.getPositions();
            for (List<Integer> position : positions) {
                assertEquals(position.size(), winLine.getReels());
            }
        }
    }
}
