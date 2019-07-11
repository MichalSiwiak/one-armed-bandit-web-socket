package com.efun.web;

import com.efun.message.*;
import com.efun.service.MessageProviderService;
import com.google.gson.Gson;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

@Controller
public class GameController {

    List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    private MessageProviderService messageProviderService;

    @RequestMapping(value = "/gameId", produces = MediaType.TEXT_PLAIN_VALUE, method = RequestMethod.GET)
    public ResponseEntity<String> getSessionId(HttpSession session) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String gameId = simpleDateFormat.format(new Date()) + session.getId();
        String md5HexGameId = DigestUtils.md5Hex(gameId).toUpperCase();
        System.out.println(md5HexGameId);
        return new ResponseEntity<>(md5HexGameId, HttpStatus.OK);
    }

    @MessageMapping("/start/{gameId}")
    @SendTo("/game/start-game/{gameId}")
    public MessageGameStart startGame(@DestinationVariable String gameId, InitParams initParams) throws Exception {
        logger.info("Init params " + initParams.toString());
        MessageGameStart messageGameStart = messageProviderService
                .startGame(initParams.getWinLinesSelected(), initParams.getReelsSelected(), gameId);
        logger.info("Message sent to client [Start Game]");
        return messageGameStart;
    }

    @MessageMapping("/spin/{gameId}")
    @SendTo("/game/spin-game/{gameId}")
    public MessageGameSpin spinGame(@DestinationVariable String gameId, SpinParams spinParams) throws Exception {
        logger.info("Spin params " + spinParams.toString());
        MessageGameSpin messageGameSpin =
                messageProviderService.executeSpin(spinParams.getRno(), spinParams.getBet(), spinParams.getAuthorizationToken());
        logger.info("Message sent to client [Spin]");
        return messageGameSpin;
    }

    @MessageMapping("/end/{gameId}")
    @SendTo("/game/end-game/{gameId}")
    public MessageGameEnd endGame(@DestinationVariable String gameId, Map<String, String> tokens) throws Exception {
        logger.info("End game params " + tokens.get("authorizationToken"));
        MessageGameEnd messageGameEnd = messageProviderService.endGame(tokens.get("authorizationToken"));
        logger.info("Message sent to client [Spin]");
        return messageGameEnd;
    }
}
