package com.efun.service;

import com.efun.config.GameConfig;
import com.efun.entity.ReelPosition;
import com.efun.entity.ReelsPositionRepository;
import com.efun.web.GameController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CheckResultServiceImpl implements CheckResultService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);
    private CacheService cacheService;
    private RnoInformationService rnoInformationService;
    private ReelsNamesService reelsNamesService;
    private ReelsPositionRepository reelsPositionRepository;
    private GameConfig gameConfig;

    public CheckResultServiceImpl(CacheService cacheService,
                                  RnoInformationService rnoInformationService,
                                  ReelsNamesService reelsNamesService,
                                  ReelsPositionRepository reelsPositionRepository,
                                  GameConfig gameConfig) {

        this.cacheService = cacheService;
        this.rnoInformationService = rnoInformationService;
        this.reelsNamesService = reelsNamesService;
        this.reelsPositionRepository = reelsPositionRepository;
        this.gameConfig = gameConfig;
    }

    // can be not one result !!!!
    @Override
    public List<List<Integer>> isReelPositionInCache(List<Integer> activeReels, int rno) {

        List<List<Integer>> symbols = new ArrayList<>();

        for (Integer activeReel : activeReels) {

            int size = gameConfig.getReels().get(activeReel).size();
            Integer gameConfigSpin = gameConfig.getSpin().get(activeReel);
            String nameOfReel = reelsNamesService.getNameOfReel(gameConfig.getReels().get(activeReel), gameConfigSpin);

            int positions = rnoInformationService.calculateSpinPositions(rno, gameConfigSpin, size);

            ReelPosition reelPosition =
                    reelsPositionRepository.findByPositionsAndMd5Hex(positions, nameOfReel);
            if (reelPosition != null) {
                //LOGGER.info("Position found");
                List<Integer> reelNumbers = reelPosition.getReelNumbers();

                symbols.add(reelNumbers);
            } else {
                //reelsNamesService.getNameOfReel()
                //LOGGER.info("Position not found");
                ReelPosition reelPositionNew = new ReelPosition();
                reelPositionNew.setMd5Hex(nameOfReel);
                reelPositionNew.setPositions(positions);
                List<Integer> movedList = rnoInformationService
                        .getMovedList(gameConfig.getReels().get(activeReel), positions);
                reelPositionNew.setReelNumbers(movedList);

                cacheService.save(reelPositionNew);

                symbols.add(reelPositionNew.getReelNumbers());
            }
        }


        List<List<Integer>> result = new ArrayList<>();
        for (List<Integer> symbol : symbols) {
            result.add(symbol.subList(0,3));
        }

        return result;
    }

    @PostConstruct
    void init() {




    }
}
