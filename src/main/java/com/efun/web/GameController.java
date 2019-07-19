package com.efun.web;

import com.efun.constants.Status;
import com.efun.entity.GameResult;
import com.efun.message.*;
import com.efun.service.GameResultService;
import com.efun.service.MessageProviderService;
import com.efun.validation.ValidationService;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class GameController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);
    private MessageProviderService messageProviderService;
    private GameResultService gameResultService;
    private ValidationService validationService;
    private MessageFactory messageFactory;

    public GameController(MessageProviderService messageProviderService,
                          GameResultService gameResultService,
                          ValidationService validationService,
                          MessageFactory messageFactory) {

        this.messageProviderService = messageProviderService;
        this.gameResultService = gameResultService;
        this.validationService = validationService;
        this.messageFactory = messageFactory;
    }

    @GetMapping("/demo-game")
    public String showTest() {
        return "game-form.html";
    }

    @GetMapping("/results")
    public String showSessions() {
        return "sessions-form.html";
    }

    @GetMapping("/description")
    public String showIndex() {
        return "description-form.html";
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
    public Message startGame(@DestinationVariable String gameId, InitParams initParams) throws Exception {
        LOGGER.info("Init params " + initParams.toString());
        Message message = messageProviderService
                .startGame(initParams.getWinLinesSelected(), initParams.getReelsSelected(), gameId);
        LOGGER.info("Message sent to client [Start Game]");

        if (message.getStatus().equals(Status.NEW)) {

            GameResult gameDto = new GameResult();
            gameDto.setGameId(gameId);
            gameDto.setAuthorizationToken(message.getAuthorizationToken());
            gameDto.setWinlineData(message.getWinlineData());
            gameDto.setStartDate(new Date());
            gameDto.setStatus(Status.NEW.toString());

            LOGGER.info("Saving changes to mongo database ...");
            gameResultService.save(gameDto);
            LOGGER.info("Inserted new record");
        } else {
            LOGGER.info("Status invalid - no records inserted");
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

        if (validationService.validateSpin(spinParams)) {
            Message message = messageProviderService.executeSpin(Integer.parseInt(spinParams.getRno()),
                    Integer.parseInt(spinParams.getBet()),
                    spinParams.getAuthorizationToken(),
                    spinParams.getGameId());

            LOGGER.info("Message sent to client [Spin]");

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

            List<Double> winList = gameResult.getWinList();
            if (winList != null) {
                winList.add(message.getWin());
                gameResult.setWinList(winList);
                gameResult.setSumOfWins(gameResult.getSumOfWins() + message.getWin());
            } else {
                winList = new ArrayList<>();
                winList.add(message.getWin());
                gameResult.setWinList(winList);
                gameResult.setSumOfWins(gameResult.getSumOfWins() + message.getWin());
            }

            gameResult.setStatus(Status.ACTIVE.toString());
            gameResult.setLastSpinDate(new Date());

            LOGGER.info("Saving changes to mongo database ...");
            gameResultService.save(gameResult);
            LOGGER.info("Changes saved of spin");
            return message;
        } else {
            return messageFactory.createMessage(Status.INCORRECT_DATA);
        }
    }

    /**
     * Method mapping request and responses of web socket when game is closing
     *
     * @param @DestinationVariable String gameId
     * @param tokens<String, String> tokens
     * @return MessageGameEnd endGame
     * @author Michał Siwiak
     */
    @MessageMapping("/end/{gameId}")
    @SendTo("/game/end-game/{gameId}")
    public Message endGame(@DestinationVariable String gameId, EndParams endParams) throws Exception {
        LOGGER.info("End game params " + endParams.getAuthorizationToken());
        Message message = messageProviderService.endGame(endParams.getAuthorizationToken(), endParams.getGameId());
        LOGGER.info("Message sent to client [END]");

        GameResult gameResult = gameResultService.getOne(gameId);
        gameResult.setStatus(Status.TERMINATED.toString());
        gameResult.setEndDate(new Date());

        LOGGER.info("Saving changes to mongo database ...");
        gameResultService.save(gameResult);
        LOGGER.info("Changes saved of end game");

        return message;
    }
}