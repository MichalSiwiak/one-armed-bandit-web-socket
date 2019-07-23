package com.efun;

import com.efun.constants.Status;
import com.efun.entity.GameResult;
import com.efun.service.GameResultService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameResultServiceTest {


    @Autowired
    private GameResultService gameResultService;

    @Test
    public void GameResultServiceTest() {

        GameResult gameResult1 = new GameResult();
        gameResult1.setStatus(Status.UNAUTHORIZED.toString());
        gameResult1.setGameId("5DB1A1C5B5F04A226780422E018219BE");

        GameResult gameResult2 = new GameResult();
        gameResult2.setStatus(Status.NEW.toString());
        gameResult2.setGameId("06D684E313E76C76764A699B3C19C2BD");

        gameResultService.save(gameResult1);
        gameResultService.save(gameResult2);

        GameResult testID1 = gameResultService.getOne("5DB1A1C5B5F04A226780422E018219BE");
        assertNotNull(testID1);
        GameResult testID2 = gameResultService.getOne("06D684E313E76C76764A699B3C19C2BD");
        assertNotNull(testID2);

        testID1.setNumberOfSpins(55);
        gameResultService.save(testID1);


        GameResult test = gameResultService.getOne("5DB1A1C5B5F04A226780422E018219BE");
        assertEquals(test.getNumberOfSpins(),55);

        gameResultService.delete("06D684E313E76C76764A699B3C19C2BD");
        gameResultService.delete("5DB1A1C5B5F04A226780422E018219BE");


    }
}