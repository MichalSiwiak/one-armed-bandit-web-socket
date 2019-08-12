package com.efun.web;

import com.efun.constants.Status;
import com.efun.entity.GameResult;
import com.efun.message.EndParams;
import com.efun.message.InitParams;
import com.efun.message.Message;
import com.efun.message.SpinParams;
import com.efun.service.GameResultService;
import com.efun.service.MessageProviderService;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class GameController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);
    private MessageProviderService messageProviderService;
    private GameResultService gameResultService;

    public GameController(MessageProviderService messageProviderService,
                          GameResultService gameResultService) {

        this.messageProviderService = messageProviderService;
        this.gameResultService = gameResultService;
    }

    @GetMapping("/simulation")
    public String showSimulation() {
        return "report-form";
    }

    @GetMapping("/demo")
    public String showTest() {
        return "game-form";
    }

    @GetMapping("/results")
    public String showSessions() {
        return "sessions-form";
    }

    @GetMapping("/")
    public String showIndex() {
        return "description-form";
    }

    @RequestMapping(value = "/gameId", produces = MediaType.TEXT_PLAIN_VALUE, method = RequestMethod.GET)
    public ResponseEntity<String> getSessionId(HttpSession session) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String gameId = simpleDateFormat.format(new Date()) + session.getId();
        String md5HexGameId = DigestUtils.md5Hex(gameId).toUpperCase();
        return new ResponseEntity<>(md5HexGameId, HttpStatus.OK);
    }

    @RequestMapping(value = "/sessions", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public ResponseEntity<List<GameResult>> getAllGames() {
        List<GameResult> gameDtoList = gameResultService.findAll();
        return new ResponseEntity<>(gameDtoList, HttpStatus.OK);
    }

    @MessageMapping("/results")
    @SendTo("/game/results-game")
    public GameResult updateGame(Map<String, String> tokens) throws Exception {
        String gameId = tokens.get("gameId");
        GameResult gameDto = gameResultService.getOne(gameId);
        return gameDto;
    }

    /**
     * Method mapping request and responses of web socket when game is creating
     *
     * @param @DestinationVariable String gameId
     * @param InitParams           initParams
     * @return MessageGameSpin spinGame
     * @author Michał Siwiak
     */
    @MessageMapping("/start/{gameId}")
    @SendTo("/game/start-game/{gameId}")
    public Message startGame(@DestinationVariable String gameId, InitParams initParams) {
        LOGGER.info("Init params " + initParams.toString());
        Message message = messageProviderService
                .startGame(initParams, gameId);
        LOGGER.info("Message sent to client [Start Game]");

        if (message.getStatus().equals(Status.NEW)) {

            GameResult gameResult = new GameResult();
            gameResult.setGameId(gameId);
            gameResult.setAuthorizationToken(message.getAuthorizationToken());
            gameResult.setWinlineData(message.getWinLineData());
            gameResult.setStartDate(new Date());
            gameResult.setStatus(Status.NEW.toString());
            LOGGER.info("Saving changes to mongo database ...");
            gameResultService.save(gameResult);
            LOGGER.info("Inserted new record");

        } else {
            LOGGER.info(message.getStatus().getMessageBody());
        }
        return message;
    }

    /**
     * Method mapping request and responses of web socket when client is executing spins
     *
     * @param gameId
     * @param spinParams []
     * @return MessageGameSpin spinGame
     * @author Michał Siwiak
     */
    @MessageMapping("/spin/{gameId}")
    @SendTo("/game/spin-game/{gameId}")
    public Message spinGame(@DestinationVariable String gameId, SpinParams spinParams) throws Exception {
        LOGGER.info("Spin params " + spinParams.toString());
        Message message = messageProviderService.executeSpin(spinParams);
        LOGGER.info("Message sent to client [Spin]");

        if (message.getStatus().equals(Status.ACTIVE)) {

            GameResult gameResult = gameResultService.getOne(gameId);
            List<Integer> spinList = gameResult.getSpinList();

            if (spinList != null) {
                spinList.add(message.getRno());
                gameResult.setSpinList(spinList);
                gameResult.setNumberOfSpins(spinList.size());
            } else {
                spinList = new ArrayList<>();
                spinList.add(message.getRno());
                gameResult.setSpinList(spinList);
                gameResult.setNumberOfSpins(spinList.size());
            }

            List<BigDecimal> winList = gameResult.getWinList();
            if (winList != null) {
                winList.add(message.getWinValue());
                gameResult.setWinList(winList);
                gameResult.setSumOfWins(gameResult.getSumOfWins().add(message.getWinValue()));
            } else {
                winList = new ArrayList<>();
                winList.add(message.getWinValue());
                gameResult.setWinList(winList);
                gameResult.setSumOfWins(message.getWinValue());
            }

            gameResult.setStatus(Status.ACTIVE.toString());
            gameResult.setLastSpinDate(new Date());

            LOGGER.info("Saving changes to mongo database ...");
            gameResultService.save(gameResult);
            LOGGER.info("Changes saved of spin");
        } else {
            LOGGER.info(message.getStatus().getMessageBody());
        }
        return message;
    }

    /**
     * Method mapping request and responses of web socket when game is closing
     *
     * @param @DestinationVariable String gameId
     * @param EndParams            endParams
     * @return MessageGameEnd endGame
     * @author Michał Siwiak
     */
    @MessageMapping("/end/{gameId}")
    @SendTo("/game/end-game/{gameId}")
    public Message endGame(@DestinationVariable String gameId, EndParams endParams) throws Exception {

        LOGGER.info("End game params " + endParams.toString());
        Message message = messageProviderService.endGame(endParams);
        LOGGER.info("Message sent to client [END]");

        if (message.getStatus().equals(Status.TERMINATED)) {

            GameResult gameResult = gameResultService.getOne(gameId);
            gameResult.setStatus(Status.TERMINATED.toString());
            gameResult.setEndDate(new Date());
            LOGGER.info("Saving changes to mongo database ...");
            gameResultService.save(gameResult);
            LOGGER.info("Changes saved of end game");

        } else {
            LOGGER.info(message.getStatus().getMessageBody());
        }
        return message;
    }
}