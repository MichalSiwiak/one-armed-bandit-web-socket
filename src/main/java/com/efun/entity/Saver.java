package com.efun.entity;

import com.efun.components.TotalWinInSpin;
import com.efun.service.CheckResultService;
import com.efun.service.WinCheckerService;
import com.efun.web.GameController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class Saver implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);

    private List<Integer> integerList;
    private Map<String, List<Integer>> combinationReelsMap;
    private String key;
    private List<Integer> allWinLines;

    @Autowired
    private WinCheckerService winCheckerService;
    @Autowired
    private CheckResultService checkResultService;
    @Autowired
    private CombinationResultRepository combinationResultRepository;


    public void setIntegerList(List<Integer> integerList) {
        this.integerList = integerList;
    }
    public void setCombinationReelsMap(Map<String, List<Integer>> combinationReelsMap) {
        this.combinationReelsMap = combinationReelsMap;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public void setAllWinLines(List<Integer> allWinLines) {
        this.allWinLines = allWinLines;
    }

    @Override
    public void run() {

        List<CombinationResult> combinationResults = new ArrayList<>();

        for (Integer integer : integerList) {
            List<List<Integer>> symbols =
                    checkResultService.getCalculatedReelPosition(combinationReelsMap.get(key), integer);
            symbols = checkResultService.getFirst3Symbols(symbols);
            TotalWinInSpin totalWinInSpin = winCheckerService.getWins(symbols, combinationReelsMap.get(key), allWinLines);

            CombinationResult combinationResult = new CombinationResult(key, combinationReelsMap.get(key),
                    integer, totalWinInSpin.getResultWinList());
            combinationResult.setSymbols(symbols);
            combinationResults.add(combinationResult);
        }

        combinationResultRepository.saveAll(combinationResults);
    }
}
