package com.efun.web;

import com.efun.constants.Status;
import com.efun.entity.GameDto;
import com.efun.message.*;
import com.efun.service.GameDtoService;
import com.efun.service.MessageProviderService;
import org.apache.commons.codec.digest.DigestUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

@Controller
public class GameController {

    List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    private MessageProviderService messageProviderService;

    @Autowired
    private GameDtoService gameDtoService;

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
    public ResponseEntity<List<GameDto>> getAllGames() {
        List<GameDto> gameDtoList = gameDtoService.findAll();
        return new ResponseEntity<>(gameDtoList, HttpStatus.OK);
    }


    @MessageMapping("/results")
    @SendTo("/game/results-game")
    public GameDto updateGame(Map<String, String> tokens) throws Exception {
        String gameId = tokens.get("gameId");
        GameDto gameDto = gameDtoService.getOne(gameId);
        return gameDto;
    }

    /**
     * Method mapping request and responses of web socket when game is creating
     *
     * @param @DestinationVariable String gameId
     * @param InitParams initParams
     * @return MessageGameSpin spinGame
     * @author Michał Siwiak
     */
    @MessageMapping("/start/{gameId}")
    @SendTo("/game/start-game/{gameId}")
    public MessageGameStart startGame(@DestinationVariable String gameId, InitParams initParams) throws Exception {
        logger.info("Init params " + initParams.toString());
        MessageGameStart messageGameStart = messageProviderService
                .startGame(initParams.getWinLinesSelected(), initParams.getReelsSelected(), gameId);
        logger.info("Message sent to client [Start Game]");

        if (messageGameStart.getStatus().equals(Status.NEW)) {

            GameDto gameDto = new GameDto();
            gameDto.setGameId(gameId);
            gameDto.setAuthorizationToken(messageGameStart.getAuthorizationToken());
            gameDto.setWinlineData(messageGameStart.getWinlineData());
            gameDto.setStartDate(new Date());
            gameDto.setStatus(Status.NEW.toString());

            logger.info("Saving changes to mongo database ...");
            gameDtoService.save(gameDto);
            logger.info("Inserted new record");
        } else {
            logger.info("Status invalid - no records inserted");
        }
        return messageGameStart;
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
    public MessageGameSpin spinGame(@DestinationVariable String gameId, SpinParams spinParams) throws Exception {
        logger.info("Spin params " + spinParams.toString());
        MessageGameSpin messageGameSpin =
                messageProviderService.executeSpin(spinParams.getRno(), spinParams.getBet(), spinParams.getAuthorizationToken());
        logger.info("Message sent to client [Spin]");

        GameDto gameDto = gameDtoService.getOne(gameId);
        List<Integer> spinList = gameDto.getSpinList();

        if (spinList != null) {
            spinList.add(messageGameSpin.getRno());
            gameDto.setSpinList(spinList);
            gameDto.setNumberOfSpins(spinList.size());
        } else {
            spinList = new ArrayList<>();
            spinList.add(messageGameSpin.getRno());
            gameDto.setSpinList(spinList);
            gameDto.setNumberOfSpins(spinList.size());
        }

        List<Double> winList = gameDto.getWinList();
        if (winList != null) {
            winList.add(messageGameSpin.getWin());
            gameDto.setWinList(winList);
            gameDto.setSumOfWins(gameDto.getSumOfWins() + messageGameSpin.getWin());
        } else {
            winList = new ArrayList<>();
            winList.add(messageGameSpin.getWin());
            gameDto.setWinList(winList);
            gameDto.setSumOfWins(gameDto.getSumOfWins() + messageGameSpin.getWin());
        }
        //cannot codec enums :(
        gameDto.setStatus(Status.ACTIVE.toString());
        gameDto.setLastSpinDate(new Date());

        Document document = new Document();
        document.put("spinList", gameDto.getSpinList());
        document.put("numberOfSpins", gameDto.getNumberOfSpins());
        document.put("winList", gameDto.getWinList());
        document.put("sumOfWins", gameDto.getSumOfWins());
        document.put("status", gameDto.getStatus());
        document.put("lastSpinDate", gameDto.getLastSpinDate());

        logger.info("Saving changes to mongo database ...");
        gameDtoService.updateOne(gameId, document);
        logger.info("Changes saved of spin");

        return messageGameSpin;
    }

    /**
     * Method mapping request and responses of web socket when game is closing
     *
     * @param @DestinationVariable String gameId
     * @param tokens<String,       String> tokens
     * @return MessageGameEnd endGame
     * @author Michał Siwiak
     */
    @MessageMapping("/end/{gameId}")
    @SendTo("/game/end-game/{gameId}")
    public MessageGameEnd endGame(@DestinationVariable String gameId, Map<String, String> tokens) throws Exception {
        logger.info("End game params " + tokens.get("authorizationToken"));
        MessageGameEnd messageGameEnd = messageProviderService.endGame(tokens.get("authorizationToken"));
        logger.info("Message sent to client [END]");

        GameDto gameDto = gameDtoService.getOne(gameId);
        gameDto.setStatus(Status.TERMINATED.toString());
        gameDto.setEndDate(new Date());

        Document document = new Document();
        document.put("status", gameDto.getStatus());
        document.put("endDate", gameDto.getEndDate());

        logger.info("Saving changes to mongo database ...");
        gameDtoService.updateOne(gameId, document);
        logger.info("Changes saved of end game");

        return messageGameEnd;
    }
}