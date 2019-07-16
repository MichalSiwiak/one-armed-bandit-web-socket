package com.efun.service;

import com.efun.config.GameConfig;
import com.efun.constants.Status;
import com.efun.entity.RandomNumberResult;
import com.efun.message.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Service
public class MessageProviderServiceImpl implements MessageProviderService {

    private Logger logger = Logger.getLogger(getClass().getName());

    @Value("${max_random_number}")
    private int maxRno;

    @Value("${max_game_number}")
    private int maxGameNumber;

    @Autowired
    private GameConfig gameConfig;

    @Autowired
    private GameCacheService gameCacheService;

    @Autowired
    private TokenServiceHandler tokenServiceHandler;

    private Map<String, MessageGameStart> sessions = new ConcurrentHashMap<>();
    //temporary collection for testing only
    private Map<String, String> tokens = new ConcurrentHashMap<>();

    /**
     * Method creating message when game is started
     * based on winLines and activeReels. GameID is generating
     * in GameController using md5 with session id and actual date.
     * Method is checking conditions while game is possible to create and fill mongo
     * database with temporary data. After that method get random rno
     * and sending this information to client. Method check also all spin and set it
     * when spin guarantee win or nor using tested static method compareEqualityOfNumbers
     *
     * @author Michał Siwiak
     * @param List<Integer> winLines - win lines selected by client
     * @param List<Integer> activeReels - active reels selected by client
     * @param String gameId
     * @return MessageGameStart messageGameStart representation of message send to client
     */
    @Override
    public MessageGameStart startGame(List<Integer> winLines, List<Integer> activeReels, String gameId) {


        if (sessions.size() > maxGameNumber - 1) {

            MessageGameStart messageGameStart = new MessageGameStart();
            messageGameStart.setStatus(Status.LIMIT_REACHED); // statuses and messages should be maintained in some enum class !!
            messageGameStart.setMessage("Maximum number of games has been exceeded limit=" + maxGameNumber);
            logger.warning("ERROR - Maximum number of games has been exceeded limit=" + maxGameNumber);
            return messageGameStart;
        } else {
            MessageGameStart messageGameStart = new MessageGameStart();

            List<List<Integer>> reels = gameConfig.getReels();
            String token = tokenServiceHandler.generateToken(sessions);

            messageGameStart.setAuthorizationToken(token);
            messageGameStart.setGameId(gameId);
            logger.info("Generated authorization=" + token);
            //for testing only
            tokens.put(gameId,token);

            for (int i = 1; i <= maxRno; i++) {

                RandomNumberResult randomNumberResult = new RandomNumberResult();
                randomNumberResult.setRandomNumber(i);
                List<List<Integer>> reelsInRandomNumber = new ArrayList<>();

                for (Integer activeReel : activeReels) {
                    List<Integer> movedList = getMovedList(reels.get(activeReel), gameConfig.getSpin().get(activeReel) * i);
                    reelsInRandomNumber.add(movedList);
                }

                int[] middleNumbers = {reelsInRandomNumber.get(0).get(1),
                        reelsInRandomNumber.get(1).get(1),
                        reelsInRandomNumber.get(2).get(1)};

                if (compareEqualityOfNumbers(middleNumbers)) {
                    //if win it's enough to choose the first index
                    if (winLines.contains(middleNumbers[0])) {
                        randomNumberResult.setWin(true);
                    }
                }

                randomNumberResult.setReelsInRandomNumber(reelsInRandomNumber);
                gameCacheService.save(randomNumberResult, token);
                logger.info("Inserted row: " + randomNumberResult);
            }

            int randomRno = getRandomNumberInRange(1, maxRno);
            logger.info("Getting number of RNO=" + randomRno);
            RandomNumberResult randomNumberResult = gameCacheService.getOne(randomRno, token);
            messageGameStart.setRno(randomNumberResult.getRandomNumber());
            logger.info("The random RNO is=" + randomNumberResult);

            //get only wins elements
            List<RandomNumberResult> wins = gameCacheService.findWins(token);
            if (wins.size() == 0) {
                messageGameStart.setStatus(Status.CONFIGURATION_NOT_ACCEPTED);
                messageGameStart.setMessage("No wins calculated for this configuration");
                logger.warning("ERROR - No wins calculated for this configuration");
            } else {
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

                winlineData.setWinLines(winLinesInResponse);
                messageGameStart.setWinlineData(winlineData);

                messageGameStart.setStatus(Status.NEW);
                messageGameStart.setMessage("Game configured successfully");
                logger.info("Game configured successfully");
                logger.info("Full response: " + messageGameStart);
                sessions.put(token, messageGameStart);
            }
            return messageGameStart;
        }
    }


    /**
     * Method creating message when client is executing spins.
     * Before this action method checks authorization
     * which is holding in another class. When authorization is positive
     * method gets spin from mongo database using gameCacheService
     * and creating message to client
     *
     * @author Michał Siwiak
     * @param int rno - rno sending by client
     * @param int bet - bet sending by client
     * @param String token - authorization sending by client
     * @return MessageGameSpin messageGameSpin representation of message send to client
     */
    @Override
    //maintain different cases whe we choose different number of reels minimal 3 not only equal 3 !!!!
    public MessageGameSpin executeSpin(int rno, int bet, String token) {

        if (tokenServiceHandler.authorizeRequest(token)) {

            // situation when rno is f.e. 501 then we get rno=1
            if (rno > maxRno) {
                rno = rno % maxRno;
            }

            MessageGameStart messageGameStart = sessions.get(token);
            MessageGameSpin messageGameSpin = new MessageGameSpin();

            messageGameSpin.setGameId(messageGameStart.getGameId());
            messageGameSpin.setWinlineData(messageGameStart.getWinlineData());

            messageGameSpin.setRno(rno);
            logger.info("Spin was started ... ");
            logger.info("Getting RandomNumberResult from database RNO=" + rno);
            RandomNumberResult randomNumberResult = gameCacheService.getOne(rno, token);

            List<List<Integer>> symbols = new ArrayList<>();
            List<Integer> reel1 = randomNumberResult.getReelsInRandomNumber().get(0).subList(0, 3);
            symbols.add(reel1);
            List<Integer> reel2 = randomNumberResult.getReelsInRandomNumber().get(1).subList(0, 3);
            symbols.add(reel2);
            List<Integer> reel3 = randomNumberResult.getReelsInRandomNumber().get(2).subList(0, 3);
            symbols.add(reel3);
            messageGameSpin.setSymbols(symbols);

            Integer index = randomNumberResult.getReelsInRandomNumber().get(0).get(1);

            double winValue;

            if (randomNumberResult.isWin()) {
                winValue = bet * gameConfig.getWinnings().get(index);
            } else {
                winValue = 0;
            }

            messageGameSpin.setWin(winValue);
            messageGameSpin.setStatus(Status.ACTIVE);
            messageGameSpin.setMessage("Spin executed successfully");
            logger.info("Spin executed successfully RNO=" + rno + " Win=" + winValue);
            logger.info("Full response: " + messageGameSpin);


            return messageGameSpin;

        } else {

            MessageGameSpin messageGameSpin = new MessageGameSpin();
            messageGameSpin.setStatus(Status.UNAUTHORIZED);
            messageGameSpin.setMessage("Unknown authorization of game");
            logger.warning("ERROR - Unknown authorization of game");

            return messageGameSpin;
        }
    }

    /**
     * Method creating message when game is closing.
     * Before this action method checks authorization
     * which is holding in another class. When authorization is positive
     * method close game, remove authorization token and drop
     * temporary cash from mongo database
     *
     * @author Michał Siwiak
     * @param String token - authorization sending by client
     * @return MessageGameEnd messageGameEnd of message send to client
     */
    @Override
    public MessageGameEnd endGame(String token) {

        if (tokenServiceHandler.authorizeRequest(token)) {
            MessageGameStart messageGameStart = sessions.get(token);
            MessageGameEnd messageGameEnd = new MessageGameEnd();

            messageGameEnd.setGameId(messageGameStart.getGameId());
            messageGameEnd.setStatus(Status.TERMINATED);
            messageGameEnd.setRno(messageGameStart.getRno());
            messageGameEnd.setMessage("The game was closed successfully");

            sessions.remove(token);
            tokenServiceHandler.removeToken(token);
            logger.info("The game was closed successfully");
            gameCacheService.removeData(token);
            logger.info("Databases of game was dropped");

            return messageGameEnd;

        } else {

            MessageGameEnd messageGameEnd = new MessageGameEnd();
            messageGameEnd.setStatus(Status.UNAUTHORIZED);
            messageGameEnd.setMessage("Unknown authorization of game");
            logger.warning("ERROR - Unknown authorization of game");

            return messageGameEnd;
        }

    }

    public Map<String, String> getTokens() {
        return tokens;
    }

    /**
     * Method checking equality of different elements using XOR logical operator
     * @author Michał Siwiak
     * @param int[] numbers - list of numbers to check its equality
     * @return true when all elements equal and false when not
     *
     */
    public static boolean compareEqualityOfNumbers(int[] numbers) {
        int length = numbers.length;
        for (int i = 0; i < length - 1; i++) {
            int check = (numbers[0] ^ numbers[i + 1]);
            if (check != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method moving list based on the given items
     *
     * @author Michał Siwiak
     * @param List<Integer> numbers - list of numbers to move
     * @param int positions - number of sliding indexes
     * @return List<Integer> numbers - new list moved by number [positions]
     *
     */
    public static List<Integer> getMovedList(List<Integer> numbers, int positions) {
        List<Integer> moved = new ArrayList<>(numbers);
        Collections.rotate(moved, positions);
        return moved;
    }

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