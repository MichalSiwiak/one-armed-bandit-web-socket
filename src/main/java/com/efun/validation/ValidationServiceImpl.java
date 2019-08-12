package com.efun.validation;

import com.efun.config.GameConfig;
import com.efun.config.WinLine;
import com.efun.message.EndParams;
import com.efun.message.InitParams;
import com.efun.message.SpinParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ValidationServiceImpl implements ValidationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationService.class);
    private GameConfig gameConfig;

    public ValidationServiceImpl(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    @Override
    public boolean validateInitParams(InitParams initParams, String gameId) {

        if (initParams.getWinLinesSelected() != null && initParams.getReelsSelected() != null && gameId != null) {

            List<List<Integer>> reels = gameConfig.getReels();
            List<Integer> activeReelsIndexes = new ArrayList<>();

            IntStream.range(0, reels.size()).forEach(activeReelsIndexes::add);

            List<Double> winnings = gameConfig.getWinnings();
            List<Integer> activeWinLinesIndexes = new ArrayList<>();

            IntStream.range(0, winnings.size()).forEach(activeWinLinesIndexes::add);
            if (activeReelsIndexes.containsAll(initParams.getReelsSelected()) && activeWinLinesIndexes.containsAll(initParams.getWinLinesSelected())) {
                return true;
            } else {
                LOGGER.warn("Incorrect initial data");
                return false;
            }
        } else {
            LOGGER.warn("Incorrect initial data");
            return false;
        }
    }

    @Override
    public boolean validateSpinParams(SpinParams spinParams) {
        if (spinParams != null && spinParams.getGameId() != null && spinParams.getAuthorizationToken() != null) {
            try {
                int bet = Integer.parseInt(spinParams.getBet());
                int rno = Integer.parseInt(spinParams.getRno());
                String gameId = spinParams.getGameId();
                String token = spinParams.getAuthorizationToken();

                boolean validation = rno > 0 && bet > 0 && gameId.length() == 32 && token.length() == 32;

                if (validation) {
                    return true;
                } else {
                    LOGGER.warn("Incorrect spin data");
                    return false;
                }
            } catch (NumberFormatException e) {
                LOGGER.warn("Incorrect spin data");
                return false;
            }
        } else {
            LOGGER.warn("Incorrect spin data");
            return false;
        }
    }

    @Override
    public boolean validateEndParams(EndParams endParams) {

        String token = endParams.getAuthorizationToken();
        String gameId = endParams.getGameId();

        if (token != null && gameId != null) {
            boolean validation = gameId.length() == 32 && token.length() == 32;
            if (validation) {
                return true;
            } else {
                LOGGER.warn("Incorrect end data");
                return false;
            }
        } else {
            LOGGER.warn("Incorrect end data");
            return false;
        }
    }

    @Override
    public boolean validateGameConfig(GameConfig gameConfig) {
        if (gameConfig == null) {
            LOGGER.warn("GameConfig should not be null");
            return false;
        }
        if (gameConfig.getReels().size() > gameConfig.getSpin().size()) {
            LOGGER.warn("Reels size should not be larger than spin size");
            return false;
        }
        if (gameConfig.getReels().size() != 5) {
            LOGGER.warn("Reels size must be equal to 5");
            return false;
        }
        Set<Integer> uniqueIds = new HashSet<>();
        gameConfig.getReels().forEach(uniqueIds::addAll);
        if (uniqueIds.size() > gameConfig.getWinnings().size()) {
            LOGGER.warn("Not all unique ids on reels have representation of winnings value");
            return false;
        }

        for (WinLine winLine : gameConfig.getWinLines()) {
            List<List<Integer>> positions = winLine.getPositions();
            for (List<Integer> position : positions) {
                if (position.size() != winLine.getReels()) {
                    LOGGER.warn("Positions in winLine " + winLine + " is not equal to reels size");
                    return false;
                }
            }
        }
        Set<Integer> uniqueIdsInWinLines = new HashSet<>();
        gameConfig.getWinLines().forEach(winLine -> uniqueIdsInWinLines.add(winLine.getIndex()));

        for (Integer index : uniqueIdsInWinLines) {
            if (!uniqueIds.contains(index)) {
                LOGGER.warn(index + " in WinLines not have representation in unique index in reels");
                return false;
            }
        }

        List<WinLine> collectSize3List = gameConfig.getWinLines().stream().filter(winLine -> winLine.getReels() == 3).collect(Collectors.toList());
        List<WinLine> collectSize4List = gameConfig.getWinLines().stream().filter(winLine -> winLine.getReels() == 4).collect(Collectors.toList());
        List<WinLine> collectSize5List = gameConfig.getWinLines().stream().filter(winLine -> winLine.getReels() == 5).collect(Collectors.toList());

        for (WinLine winLine : collectSize3List) {
            List<List<Integer>> positions = winLine.getPositions();
            for (List<Integer> position : positions) {
                for (Integer integer : position) {
                    if (integer > 8) {
                        LOGGER.warn(integer + " could not be in reels with size 3");
                        return false;
                    }
                }
            }
        }
        for (WinLine winLine : collectSize4List) {
            List<List<Integer>> positions = winLine.getPositions();
            for (List<Integer> position : positions) {
                for (Integer integer : position) {
                    if (integer > 11) {
                        LOGGER.warn(integer + " could not be in reels with size 4");
                        return false;
                    }
                }
            }
        }
        for (WinLine winLine : collectSize5List) {
            List<List<Integer>> positions = winLine.getPositions();
            for (List<Integer> position : positions) {
                for (Integer integer : position) {
                    if (integer > 14) {
                        LOGGER.warn(integer + " could not be in reels with size 5");
                        return false;
                    }
                }
            }
        }

        Set<WinLine> allWinLines = new HashSet<>();
        for (WinLine winLine : gameConfig.getWinLines()) {
            if (!allWinLines.add(winLine)) {
                LOGGER.warn("Duplicate WinLine " + winLine);
                return false;
            }
        }

        return true;
    }
}