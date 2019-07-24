package com.efun.service;

import com.efun.components.TotalWinInSpin;
import com.efun.config.GameConfig;
import com.efun.entity.CombinationResult;
import com.efun.entity.CombinationResultRepository;
import com.efun.web.GameController;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class CombinationServiceImpl implements CombinationService {


    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);

    private RnoInformationService rnoInformationService;
    private CheckResultService checkResultService;
    private WinCheckerService winCheckerService;
    private GameConfig gameConfig;
    private MongoTemplate mongoTemplate;
    private CombinationResultRepository combinationResultRepository;


    public CombinationServiceImpl(RnoInformationService rnoInformationService,
                                  CheckResultService checkResultService,
                                  WinCheckerService winCheckerService,
                                  GameConfig gameConfig,
                                  MongoTemplate mongoTemplate,
                                  CombinationResultRepository combinationResultRepository) {

        this.rnoInformationService = rnoInformationService;
        this.checkResultService = checkResultService;
        this.winCheckerService = winCheckerService;
        this.gameConfig = gameConfig;
        this.mongoTemplate = mongoTemplate;
        this.combinationResultRepository = combinationResultRepository;
    }

    //run this method if game configuration is updated
    //@PostConstruct
    private void saveAllCombinationsToDatabase() {
        //dropping collection to clear old data
        mongoTemplate.dropCollection(CombinationResult.class);
        List<Integer> allWinLines = new ArrayList<>();
        Set<Integer> uniqueIds = new HashSet<>();
        List<List<Integer>> reels = gameConfig.getReels();

        for (List<Integer> reel : reels) {
            for (Integer id : reel) {
                uniqueIds.add(id);
            }
        }
        for (Integer uniqueId : uniqueIds) {
            allWinLines.add(uniqueId);
        }

        Map<String, List<Integer>> combinationReelsMap = generateCombinationReels();
        LOGGER.info("Found combination of reels: " + combinationReelsMap.size());
        for (String key : combinationReelsMap.keySet()) {

            int periodicity = rnoInformationService.
                    calculateCyclicalPositionOfReels(combinationReelsMap.get(key));
            LOGGER.info("Please wait - possible RNOs [size: " + periodicity + "] of combination "
                    + combinationReelsMap.get(key) + " is saving ...");
            for (int i = 1; i <= periodicity; i++) {
                List<List<Integer>> symbols =
                        checkResultService.getReelPositionFromCacheOrCalculateAndSave(combinationReelsMap.get(key), i);
                symbols = checkResultService.getFirst3Symbols(symbols);
                TotalWinInSpin totalWinInSpin = winCheckerService.getWins(symbols, combinationReelsMap.get(key), allWinLines);

                CombinationResult combinationResult = new CombinationResult(key, combinationReelsMap.get(key),
                        i, totalWinInSpin.getResultWinList());
                combinationResult.setSymbols(symbols);
                mongoTemplate.save(combinationResult);
            }
        }
    }

    @Override
    public Map<String, List<Integer>> generateCombinationReels() {
        Map<String, List<Integer>> combinationReelsMap = new HashMap<>();
        for (List<Integer> list : generate(5, 3)) {
            combinationReelsMap.put(getNameOfCombinationReels(list), list);
        }
        for (List<Integer> list : generate(5, 4)) {
            combinationReelsMap.put(getNameOfCombinationReels(list), list);
        }
        for (List<Integer> list : generate(5, 5)) {
            combinationReelsMap.put(getNameOfCombinationReels(list), list);
        }
        return combinationReelsMap;
    }

    @Override
    public String getNameOfCombinationReels(List<Integer> activeReels) {
        StringBuffer reelName = new StringBuffer();
        for (Integer integer : activeReels) {
            reelName.append(integer);
        }
        return DigestUtils.md5Hex(reelName.toString()).toUpperCase();
    }

    @Override
    public List<CombinationResult> getTotalPossibleWinnings(String combinationId, List<Integer> activeWinLines) {
        List<CombinationResult> list = combinationResultRepository
                .findByCombinationIdAndNumbersOfWinsContains(combinationId, activeWinLines);
        return list;
    }

    @Override
    public CombinationResult getRnoPossibleWinnings(String combinationId, int rno) {
        return combinationResultRepository.findByCombinationIdAndRno(combinationId, rno);
    }

    private List<List<Integer>> generate(int n, int r) {
        List<List<Integer>> results = new ArrayList<>();
        Iterator<int[]> iterator = CombinatoricsUtils.combinationsIterator(n, r);
        while (iterator.hasNext()) {
            final int[] combination = iterator.next();
            results.add(Arrays.stream(combination).boxed().collect(Collectors.toList()));
        }
        return results;
    }
}
