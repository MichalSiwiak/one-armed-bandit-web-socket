package com.efun.service;

import com.efun.components.ResultWin;
import com.efun.components.SimulationReportEnd;
import com.efun.components.SimulationReportInit;
import com.efun.components.TotalWinInSpin;
import com.efun.config.GameConfig;
import com.efun.entity.CombinationResult;
import com.efun.entity.CombinationResultRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class SimulationServiceImpl implements SimulationService {

    private CombinationResultRepository combinationResultRepository;
    private RnoInformationService rnoInformationService;
    private CombinationService combinationService;
    private WinCheckerService winCheckerService;
    private GameConfig gameConfig;

    public SimulationServiceImpl(CombinationResultRepository combinationResultRepository,
                                 RnoInformationService rnoInformationService,
                                 CombinationService combinationService,
                                 WinCheckerService winCheckerService,
                                 GameConfig gameConfig) {

        this.combinationResultRepository = combinationResultRepository;
        this.rnoInformationService = rnoInformationService;
        this.combinationService = combinationService;
        this.winCheckerService = winCheckerService;
        this.gameConfig = gameConfig;
    }


    //dodac zmienna wygrana na każdym spinie i zaprezentowac dane na wykresie
    @Override
    public SimulationReportEnd generateLotOFSpins(SimulationReportInit simulationReportInit) {

        int periodicity = rnoInformationService.calculateCyclicalPositionOfReels(simulationReportInit.getActiveReels());
        String nameOfCombinationReels = combinationService.getNameOfCombinationReels(simulationReportInit.getActiveReels());

        int start = simulationReportInit.getStart();
        int size = simulationReportInit.getSize();

        start = start % periodicity;
        List<Integer> head = new ArrayList<>();
        List<Integer> tail = new ArrayList<>();
        List<Integer> body = new ArrayList<>();
        int bodyCount;

        for (int i = 1; i <= periodicity; i++) {
            body.add(i);
        }
        if (start != 0) {
            for (int i = start; i <= start + (periodicity - start); i++) {
                if (head.size() < size) {
                    if (i % periodicity == 0) {
                        head.add(periodicity);
                    } else {
                        head.add(i % periodicity);
                    }
                }
            }
            bodyCount = (size - head.size()) / periodicity;
            int tailStart = head.size() + bodyCount * periodicity + 1;

            for (int i = tailStart; i <= size; i++) {
                if (i % periodicity == 0) {
                    tail.add(periodicity);
                } else {
                    tail.add(i % periodicity);
                }
            }
        } else {
            bodyCount = size / periodicity;
            int tailStart = bodyCount * periodicity + 1;
            for (int i = tailStart; i <= size; i++) {
                if (i % periodicity == 0) {
                    tail.add(periodicity);
                } else {
                    tail.add(i % periodicity);
                }
            }
        }

        LinkedList<CombinationResult> total = new LinkedList<>();
        if (head.size() != 0) {
            List<CombinationResult> headResults =
                    combinationResultRepository.findByRnoIsInAndCombinationIdEquals(head, nameOfCombinationReels);
            total.addAll(headResults);
        }
        if (bodyCount != 0) {
            List<CombinationResult> bodyResults
                    = combinationResultRepository.findByRnoIsInAndCombinationIdEquals(body, nameOfCombinationReels);
            for (int i = 1; i <= bodyCount; i++) {
                total.addAll(bodyResults);
            }
        }
        if (tail.size() != 0) {
            List<CombinationResult> tailResults =
                    combinationResultRepository.findByRnoIsInAndCombinationIdEquals(tail, nameOfCombinationReels);
            total.addAll(tailResults);
        }

        SimulationReportEnd simulationReportEnd = new SimulationReportEnd();
        int numberOfWins = 0;
        BigDecimal sumOfWins = new BigDecimal("0");
        BigDecimal balance = simulationReportInit.getStartingBalance();


        simulationReportEnd.setNumberOfSpins(size);
        simulationReportEnd.setBet(simulationReportInit.getBet());
        Set<Integer> indexesWins = new HashSet<>();


        for (CombinationResult combinationResult : total) {
            TotalWinInSpin totalWinInSpin = winCheckerService.getWins(combinationResult.getSymbols(),
                    simulationReportInit.getActiveReels(), simulationReportInit.getActiveWinLines());
            combinationResult.setResultWinList(totalWinInSpin.getResultWinList());
            List<Integer> numbersOfWins = new ArrayList<>();


            if (totalWinInSpin.getResultWinList().size() == 0) {
                combinationResult.setWin(false);
            } else {

                for (ResultWin resultWin : totalWinInSpin.getResultWinList()) {
                    numbersOfWins.add(resultWin.getIndex());
                }
                combinationResult.setWin(true);

            }
            combinationResult.setNumbersOfWins(numbersOfWins);
            for (Integer numbersOfWin : numbersOfWins) {
                indexesWins.add(numbersOfWin);
            }

            List<ResultWin> resultWinList = combinationResult.getResultWinList();
            for (ResultWin resultWin : resultWinList) {
                sumOfWins = sumOfWins.add(new BigDecimal(String.valueOf(resultWin.getMultiply()))
                        .multiply(new BigDecimal(String.valueOf(gameConfig.getWinnings().get(resultWin.getIndex())))));

            }
            numberOfWins = numberOfWins + resultWinList.size();
            balance = balance.subtract(simulationReportInit.getBet());
        }


        simulationReportEnd.setIndexesWins(indexesWins);
        simulationReportEnd.setNumberOfWins(numberOfWins);
        simulationReportEnd.setSumOfWins(sumOfWins);
        simulationReportEnd.setValueOfWins(sumOfWins.multiply(simulationReportInit.getBet()));
        simulationReportEnd.setStartingBalance(simulationReportInit.getStartingBalance());
        simulationReportEnd.setEndBalance(balance.add(simulationReportEnd.getValueOfWins()));
        //simulationReportEnd.setCombinationResults(total);

        return simulationReportEnd;
    }

    private static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("Max must be greater than min");
        }
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }
}
