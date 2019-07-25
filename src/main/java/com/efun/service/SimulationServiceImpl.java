package com.efun.service;

import com.efun.components.ResultWin;
import com.efun.components.TotalWinInSpin;
import com.efun.entity.CombinationResult;
import com.efun.entity.CombinationResultRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class SimulationServiceImpl implements SimulationService {

    private CombinationResultRepository combinationResultRepository;
    private RnoInformationService rnoInformationService;
    private CombinationService combinationService;
    private WinCheckerService winCheckerService;

    public SimulationServiceImpl(CombinationResultRepository combinationResultRepository,
                                 RnoInformationService rnoInformationService,
                                 CombinationService combinationService,
                                 WinCheckerService winCheckerService) {

        this.combinationResultRepository = combinationResultRepository;
        this.rnoInformationService = rnoInformationService;
        this.combinationService = combinationService;
        this.winCheckerService = winCheckerService;
    }

    @Override
    public List<CombinationResult> generateLotOFSpins(int start, int end, int size, List<Integer> activeReels, List<Integer> activeWinLines) {

        int randomNumber = getRandomNumberInRange(start, end);
        int periodicity = rnoInformationService.calculateCyclicalPositionOfReels(activeReels);
        String nameOfCombinationReels = combinationService.getNameOfCombinationReels(activeReels);
        List<List<Integer>> list = new ArrayList<>();
        List<Integer> element = new ArrayList<>();


        for (int i = randomNumber; i < randomNumber + size; i++) {
            if ((i % periodicity) == 0) {
                element.add(periodicity);
                list.add(element);
                element = new ArrayList<>();
            } else {
                element.add(i % periodicity);
            }
        }

        list.add(element);
/*
        int check = 0;
        List<Integer> head = new ArrayList<>();
        for (int i = randomNumber; i <= periodicity; i++) {
            if (size >= head.size()) {
                head.add(i);
                check++;
            }
        }
        List<Integer> body = new ArrayList<>();
        for (int i = periodicity + 1; i <= periodicity * 2; i++) {
            if (size >= head.size() + body.size()) {
                body.add(i);
                check++;
            }
        }*/

        List<CombinationResult> total = new ArrayList<>();

        for (List<Integer> integers : list) {
            total.addAll(combinationResultRepository
                    .findByRnoIsInAndCombinationIdEquals(integers, nameOfCombinationReels));
        }

        for (CombinationResult combinationResult : total) {
            TotalWinInSpin totalWinInSpin = winCheckerService.getWins(combinationResult.getSymbols(),
                    activeReels, activeWinLines);
            combinationResult.setResultWinList(totalWinInSpin.getResultWinList());

            if (totalWinInSpin.getResultWinList().size() == 0) {
                combinationResult.setWin(false);
            } else {
                List<Integer> numbersOfWins = new ArrayList<>();
                for (ResultWin resultWin : totalWinInSpin.getResultWinList()) {
                    numbersOfWins.add(resultWin.getIndex());
                }
                combinationResult.setWin(true);
                combinationResult.setNumbersOfWins(numbersOfWins);
            }
        }

        return total;
    }

    private static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("Max must be greater than min");
        }
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }
}
