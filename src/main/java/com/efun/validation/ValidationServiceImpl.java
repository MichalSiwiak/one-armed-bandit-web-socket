package com.efun.validation;

import com.efun.config.GameConfig;
import com.efun.config.WinLine;
import com.efun.message.EndParams;
import com.efun.message.InitParams;
import com.efun.message.SpinParams;
import com.efun.web.GameController;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
            LOGGER.warn("GameConfig should not be null ");
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

        return true;
        // to add winLine validation
        /*List<WinLine> winLines = gameConfig.getWinLines();
        for (WinLine winLine : winLines) {
            List<List<Integer>> positions = winLine.getPositions();
            for (List<Integer> position : positions) {
                assertEquals(position.size(), winLine.getReels());
            }
        }*/
    }

}