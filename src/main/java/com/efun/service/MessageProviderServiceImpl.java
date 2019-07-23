package com.efun.service;

import com.efun.components.TotalWinInSpin;
import com.efun.components.WinLineData;
import com.efun.config.GameConfig;
import com.efun.constants.Status;
import com.efun.message.*;
import com.efun.validation.ValidationService;
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
    private ValidationService validationService;


    public MessageProviderServiceImpl(MessageFactory messageFactory,
                                      GameConfig gameConfig,
                                      TokenServiceHandler tokenServiceHandler,
                                      CheckResultService checkResultService,
                                      WinCheckerService winCheckerService,
                                      ValidationService validationService) {

        this.messageFactory = messageFactory;
        this.gameConfig = gameConfig;
        this.tokenServiceHandler = tokenServiceHandler;
        this.checkResultService = checkResultService;
        this.winCheckerService = winCheckerService;
        this.validationService = validationService;

    }

    private Map<String, Message> sessions = new ConcurrentHashMap<>();

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
    public Message startGame(InitParams initParams, String gameId) {

        boolean validation = validationService.validateInitParams(initParams, gameId);
        if (validation) {
            if (sessions.size() > maxGameNumber - 1) {
                Message messageError = messageFactory.createMessage(Status.LIMIT_REACHED);
                messageError.setMessage(messageError.getStatus().getMessageBody());
                LOGGER.warn("ERROR - Maximum number of games has been exceeded limit=" + maxGameNumber);
                return messageError;
            } else {
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
                Message message = messageFactory.createMessage(Status.NEW);
                String token = tokenServiceHandler.generateToken(gameId, sessions);

                message.setAuthorizationToken(token);
                message.setGameId(gameId);
                LOGGER.info("Generated authorization=" + token);
                int randomRno = getRandomNumberInRange(1, maxRno);
                message.setRno(randomRno);
                LOGGER.info("The random RNO is=" + randomRno);

                WinLineData winLineData = new WinLineData(winCheckerService.
                        getWinLinesData(initParams.getWinLinesSelected(), initParams.getReelsSelected()));
                message.setWinLineData(winLineData);
                message.setActiveWinLines(initParams.getWinLinesSelected());
                message.setActiveReels(initParams.getReelsSelected());
                message.setBalance(new BigDecimal("5000"));
                message.setMessage(message.getStatus().getMessageBody());
                LOGGER.info("Game configured successfully");
                LOGGER.info("Full response: " + message);
                sessions.put(token, message);
                return message;
            }
        } else {
            return incorrectData();
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
        boolean validation = validationService.validateSpinParams(spinParams);

        if (validation) {
            if (tokenServiceHandler.authorizeRequest(spinParams.getGameId(), spinParams.getAuthorizationToken(), sessions)) {

                Message messageStarted = sessions.get(spinParams.getAuthorizationToken());
                Message messageSpin = messageFactory.createMessage(Status.ACTIVE);
                messageSpin.setGameId(messageStarted.getGameId());
                messageSpin.setWinLineData(messageStarted.getWinLineData());

                messageSpin.setRno(Integer.parseInt(spinParams.getRno()));
                LOGGER.info("Spin was started ... ");
                LOGGER.info("Getting RandomNumberResult from database RNO=" + Integer.parseInt(spinParams.getRno()));

                List<Integer> activeReels = messageStarted.getActiveReels();
                List<Integer> activeWinLines = messageStarted.getActiveWinLines();

                List<List<Integer>> symbols =
                        checkResultService.getReelPositionFromCacheOrCalculateAndSave(activeReels, Integer.parseInt(spinParams.getRno()));

                messageStarted.setBalance(messageStarted.getBalance().subtract(new BigDecimal(spinParams.getBet())));
                symbols = checkResultService.getFirst3Symbols(symbols);
                messageSpin.setSymbols(symbols);

                boolean isWin = winCheckerService.isWin(symbols, activeReels, activeWinLines);
                messageSpin.setWin(isWin);

                TotalWinInSpin wins = winCheckerService.getWins(symbols, activeReels, activeWinLines);
                BigDecimal valueInSpinWithBet = wins.getTotalMultiply().multiply(new BigDecimal(spinParams.getBet()));

                messageStarted.setBalance(messageStarted.getBalance().add(valueInSpinWithBet));
                messageSpin.setBalance(messageStarted.getBalance());

                messageSpin.setWinValue(valueInSpinWithBet);
                messageSpin.setMessage(messageSpin.getStatus().getMessageBody());

                LOGGER.info("Spin executed successfully RNO=" + spinParams.getRno() + " Win=" + valueInSpinWithBet);
                LOGGER.info("Full response: " + messageSpin);
                return messageSpin;

            } else {
                Message messageError = messageFactory.createMessage(Status.UNAUTHORIZED);
                messageError.setMessage(messageError.getStatus().getMessageBody());
                LOGGER.warn("ERROR - Unknown authorization of game");
                return messageError;
            }
        } else {
            return incorrectData();
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

        boolean validation = validationService.validateEndParams(token, gameId);

        if (validation) {
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
        } else {
            return incorrectData();
        }
    }

    private Message incorrectData() {
        Message messageError = messageFactory.createMessage(Status.INCORRECT_DATA);
        messageError.setMessage(messageError.getStatus().getMessageBody());
        LOGGER.warn("ERROR - Incorrect data has been sent");
        return messageError;
    }

    public static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("Max must be greater than min");
        }
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }
}