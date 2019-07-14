package com.efun;

import com.efun.config.GameConfig;
import com.efun.service.MessageProviderService;
import com.efun.service.MessageProviderServiceImpl;
import com.efun.service.TokenServiceHandler;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OneArmedBanditSpringBootApplicationTests {

    @Autowired
    GameConfig gameConfig;

    @Autowired
    TokenServiceHandler tokenServiceHandler;


    @Test
    public void contextLoads() {
    }

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
    }

    @Test
    public void tokenServiceHandlerTest() {

        String string = "2019-07-09 11:58:27.3440";
        String hash = "DB2681725AAF583F23ED7F55DF4025E7";
        assertEquals(hash, DigestUtils.md5Hex(string).toUpperCase());

        String tokenHashed = tokenServiceHandler.generateToken(new HashMap<>());
        assertEquals(32, tokenHashed.length());
        assertEquals(true, tokenServiceHandler.authorizeRequest(tokenHashed));
        tokenServiceHandler.removeToken(tokenHashed);
        assertEquals(false, tokenServiceHandler.authorizeRequest(tokenHashed));

        String token1 = tokenServiceHandler.generateToken(new HashMap<>());
        String token2 = tokenServiceHandler.generateToken(new HashMap<>());
        String token3 = tokenServiceHandler.generateToken(new HashMap<>());

        assertEquals(true, tokenServiceHandler.authorizeRequest(token1));
        assertEquals(true, tokenServiceHandler.authorizeRequest(token2));
        assertEquals(true, tokenServiceHandler.authorizeRequest(token3));

        tokenServiceHandler.removeToken(token2);
        assertEquals(false, tokenServiceHandler.authorizeRequest(token2));
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
