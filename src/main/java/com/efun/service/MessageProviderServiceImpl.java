package com.efun.service;

import com.efun.components.Pocket;
import com.efun.config.GameConfig;
import com.efun.constants.Status;
import com.efun.message.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MessageProviderServiceImpl implements MessageProviderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProviderService.class);

    @Value("${max_random_number}")
    private int maxRno;

    @Value("${max_game_number}")
    private int maxGameNumber;

    private MessageFactory messageFactory;
    private GameConfig gameConfig;
    private TokenServiceHandler tokenServiceHandler;
    private CheckResultService checkResultService;
    private WinCheckerService winCheckerService;
    private Pocket pocket;


    private List<Integer> winLines = new ArrayList<>();
    private List<Integer> activeReels = new ArrayList<>();

    public MessageProviderServiceImpl(MessageFactory messageFactory,
                                      GameConfig gameConfig,
                                      TokenServiceHandler tokenServiceHandler,
                                      CheckResultService checkResultService,
                                      WinCheckerService winCheckerService,
                                      Pocket pocket) {

        this.messageFactory = messageFactory;
        this.gameConfig = gameConfig;
        this.tokenServiceHandler = tokenServiceHandler;
        this.checkResultService = checkResultService;
        this.winCheckerService = winCheckerService;
        this.pocket = pocket;
    }

    private Map<String, Message> sessions = new ConcurrentHashMap<>();
    //temporary collection for testing only
    //private Map<String, String> tokens = new ConcurrentHashMap<>();

    /**
     * Method creating message when game is started
     * based on winLines and activeReels. GameID is generating
     * in GameController using md5 with session id and actual date.
     * Method is checking conditions while game is possible to create and fill mongo
     * database with temporary data. After that method get random rno
     * and sending this information to client. Method check also all spin and set it
     * when spin guarantee win or nor using tested static method compareEqualityOfNumbers
     *
     * @param List<Integer> winLines - win lines selected by client
     * @param List<Integer> activeReels - active reels selected by client
     * @param String        gameId
     * @return MessageGameStart messageGameStart representation of message send to client
     * @author Michał Siwiak
     */
    @Override
    public Message startGame(List<Integer> winLines, List<Integer> activeReels, String gameId) {

        this.winLines = winLines;
        this.activeReels = activeReels;

        if (sessions.size() > maxGameNumber - 1) {

            Message messageError = messageFactory.createMessage(Status.LIMIT_REACHED);
            messageError.setMessage(messageError.getStatus().getMessageBody());
            LOGGER.warn("ERROR - Maximum number of games has been exceeded limit=" + maxGameNumber);
            return messageError;

        } else {
            Message message = messageFactory.createMessage(Status.NEW);
            String token = tokenServiceHandler.generateToken(gameId, sessions);

            message.setAuthorizationToken(token);
            message.setGameId(gameId);
            LOGGER.info("Generated authorization=" + token);
            //for testing only
            //tokens.put(gameId, token);

            int randomRno = getRandomNumberInRange(1, maxRno);
            message.setRno(randomRno);
            LOGGER.info("The random RNO is=" + randomRno);

            //get only wins elements
          /*  List<RandomNumberResult> wins = gameCacheService.findWins(token);
            double quantity = roundDouble2precision((double) wins.size() / (double) maxRno, 4);
            WinLineData winlineData = new WinLineData();
            winlineData.setQuantity(quantity);
            List<WinLine> winLinesInResponse = new ArrayList<>();
            Map<Integer, List<Integer>> positionsMap = new HashMap<>();

            for (RandomNumberResult win : wins) {
                //if win it's enough to choose the first index
                Integer index = win.getReelsInRandomNumber().get(0).get(1);
                if (!positionsMap.containsKey(index)) {
                    List<Integer> positions = new ArrayList<>();
                    positions.add(win.getRandomNumber());
                    positionsMap.put(index, positions);
                } else {
                    List<Integer> positions = positionsMap.get(index);
                    positions.add(win.getRandomNumber());
                }
            }
            for (Integer index : positionsMap.keySet()) {
                WinLine winLine = new WinLine();
                winLine.setIndex(index);
                winLine.setMultiply(gameConfig.getWinnings().get(index));
                winLine.setPositions(positionsMap.get(index));
                winLinesInResponse.add(winLine);
            }
            winlineData.setWinLines(winLinesInResponse);*/

            //message.setWinlineData(winlineData);
            message.setBalance(pocket.getBalance());
            message.setMessage(message.getStatus().getMessageBody());
            LOGGER.info("Game configured successfully");
            LOGGER.info("Full response: " + message);
            sessions.put(token, message);

            return message;
/*
            if (wins.size() != 0) {
               //To Implement
            } else {
                Message messageError = messageFactory.createMessage(Status.CONFIGURATION_NOT_ACCEPTED);
                messageError.setStatus(Status.CONFIGURATION_NOT_ACCEPTED);
                messageError.setMessage(messageError.getStatus().getMessageBody());
                LOGGER.warn("ERROR - No wins calculated for this configuration");
                return messageError;

            }*/

        }
    }


    /**
     * Method creating message when client is executing spins.
     * Before this action method checks authorization
     * which is holding in another class. When authorization is positive
     * method gets spin from mongo database using gameCacheService
     * and creating message to client
     *
     * @param int    rno - rno sending by client
     * @param int    bet - bet sending by client
     * @param String token - authorization sending by client
     * @return MessageGameSpin messageGameSpin representation of message send to client
     * @author Michał Siwiak
     */
    @Override
    public Message executeSpin(SpinParams spinParams) {

        if (tokenServiceHandler.authorizeRequest(spinParams.getGameId(), spinParams.getAuthorizationToken(), sessions)) {

            Message messageStarted = sessions.get(spinParams.getAuthorizationToken());
            Message messageSpin = messageFactory.createMessage(Status.ACTIVE);
            messageSpin.setGameId(messageStarted.getGameId());
            messageSpin.setWinlineData(messageStarted.getWinlineData());

            //change it in future
            messageSpin.setRno(Integer.parseInt(spinParams.getRno()));
            LOGGER.info("Spin was started ... ");
            LOGGER.info("Getting RandomNumberResult from database RNO=" + Integer.parseInt(spinParams.getRno()));

            List<List<Integer>> reelPositionInCache =
                    checkResultService.isReelPositionInCache(activeReels, Integer.parseInt(spinParams.getRno()));

            pocket.setBalance(pocket.getBalance().subtract(new BigDecimal(spinParams.getBet())));
            messageSpin.setBalance(pocket.getBalance());
            messageSpin.setSymbols(checkResultService.getFirst3Symbols(reelPositionInCache));

            boolean win = winCheckerService.isWin(reelPositionInCache, gameConfig.getWins());

            BigDecimal winInSpin = winCheckerService.getWinInSpin();
            BigDecimal valueInSpin = winInSpin.multiply(new BigDecimal(spinParams.getBet()));
            pocket.setBalance(pocket.getBalance().add(valueInSpin));

            messageSpin.setBalance(pocket.getBalance());
            messageSpin.setWin(valueInSpin);
            messageSpin.setMessage(messageSpin.getStatus().getMessageBody());

            LOGGER.info("Spin executed successfully RNO=" + spinParams.getRno() + " Win=" + winInSpin);
            LOGGER.info("Full response: " + messageSpin);

            return messageSpin;

        } else {
            Message messageError = messageFactory.createMessage(Status.UNAUTHORIZED);
            messageError.setMessage(messageError.getStatus().getMessageBody());
            LOGGER.warn("ERROR - Unknown authorization of game");
            return messageError;
        }
    }

    /**
     * Method creating message when game is closing.
     * Before this action method checks authorization
     * which is holding in another class. When authorization is positive
     * method close game, remove authorization token and drop
     * temporary cash from mongo database
     *
     * @param String token - authorization sending by client
     * @return MessageGameEnd messageGameEnd of message send to client
     * @author Michał Siwiak
     */
    @Override
    public Message endGame(String token, String gameId) {

        if (tokenServiceHandler.authorizeRequest(gameId, token, sessions)) {
            Message messageActive = sessions.get(token);
            Message messageEnd = messageFactory.createMessage(Status.TERMINATED);

            messageEnd.setGameId(messageActive.getGameId());
            messageEnd.setRno(messageActive.getRno());
            messageEnd.setMessage(messageEnd.getStatus().getMessageBody());

            sessions.remove(token);
            tokenServiceHandler.removeToken(token);
            LOGGER.info("The game was closed successfully");
            LOGGER.info("Databases of game was dropped");

            return messageEnd;

        } else {
            Message messageError = messageFactory.createMessage(Status.UNAUTHORIZED);
            messageError.setMessage(messageError.getStatus().getMessageBody());
            LOGGER.warn("ERROR - Unknown authorization of game");
            return messageError;
        }
    }

    /*public Map<String, String> getTokens() {
        return tokens;
    }
    */

    public static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("Max must be greater than min");
        }
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }

    public double roundDouble2precision(double value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}