package com.efun;

import com.efun.config.GameConfig;
import com.efun.config.GameConfigSingletonBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class ConfigTest {

    GameConfig gameConfig;

    @Before
    public void setup() {
        gameConfig = GameConfigSingletonBuilder.getInstance();
    }

    @Test
    public void correctnessOfConfigurationFileTest() {
        //checking if config file is not null
        assertNotNull(gameConfig);
        System.out.println(ReflectionToStringBuilder.toString(gameConfig, ToStringStyle.MULTI_LINE_STYLE));

        //number of reels must equal to array size of spins
        assertEquals(gameConfig.getReels().size(), gameConfig.getSpin().size());

        //now we calculate unique ids from reels:
        Set<Byte> uniqueIds = new HashSet<>();
        List<List<Byte>> reels = gameConfig.getReels();

        for (List<Byte> reel : reels) {
            for (Byte id : reel) {
                uniqueIds.add(id);
            }
        }

        //and size of set of unique ids must equal to winnings array
        assertEquals(uniqueIds.size(), gameConfig.getWinnings().size());
    }
}