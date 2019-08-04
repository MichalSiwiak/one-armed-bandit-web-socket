package com.efun.service;

import com.efun.components.ResultWin;
import com.efun.components.TotalWinInSpin;
import com.efun.config.GameConfig;
import com.efun.config.WinLine;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service
public class WinCheckerServiceImpl implements WinCheckerService {


    private GameConfig gameConfig;

    public WinCheckerServiceImpl(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }


    @Override
    public TotalWinInSpin getWins(List<List<Integer>> symbols,
                                  List<Integer> activeReels,
                                  List<Integer> activeWinLines) {

        TotalWinInSpin totalWinInSpin = new TotalWinInSpin();
        totalWinInSpin.setTotalMultiply(new BigDecimal("0"));
        List<ResultWin> winsList = new ArrayList<>();

        List<WinLine> winLinesData = getWinLinesData(activeWinLines, activeReels);
        List<Integer> symbolsToOneArray = createSymbolsToOneArray(symbols);

        for (WinLine winLine : winLinesData) {
            List<List<Integer>> indexesList = winLine.getPositions();
            for (List<Integer> indexes : indexesList) {
                List<Integer> equalValues = collectListFromIndexes(indexes, symbolsToOneArray);
                boolean checkWin = compareEqualityOfNumbers(winLine.getIndex(), equalValues);
                if (checkWin) {
                    ResultWin resultWin = new ResultWin(
                            winLine.getIndex(),
                            winLine.getMultiply(),
                            winLine.getReels(),
                            equalValues,
                            indexes);
                    winsList.add(resultWin);
                }
            }
        }

        if (gameConfig.isFilterOnlyHighestResultsInWinLine())
            filterOnlyHighestResultsInWinLine(winsList);

        for (ResultWin resultWin : winsList) {
            BigDecimal multiply = new BigDecimal(String.valueOf(resultWin.getMultiply()))
                    .multiply(new BigDecimal(String.valueOf(gameConfig.getWinnings().get(resultWin.getIndex()))));

            totalWinInSpin.setTotalMultiply(totalWinInSpin.getTotalMultiply().add(multiply));
        }
        totalWinInSpin.setResultWinList(winsList);
        return totalWinInSpin;
    }

    @Override
    public boolean isWin(List<List<Integer>> symbols,
                         List<Integer> activeReels,
                         List<Integer> activeWinLines) {

        List<WinLine> winLinesData = getWinLinesData(activeWinLines, activeReels);
        List<Integer> symbolsToOneArray = createSymbolsToOneArray(symbols);

        for (WinLine winLine : winLinesData) {
            List<List<Integer>> indexesList = winLine.getPositions();
            for (List<Integer> indexes : indexesList) {
                List<Integer> equalValues = collectListFromIndexes(indexes, symbolsToOneArray);
                boolean checkWin = compareEqualityOfNumbers(winLine.getIndex(), equalValues);
                if (checkWin) {
                    return true;
                }
            }
        }

        return false;
    }


    @Override
    public List<WinLine> getWinLinesData(List<Integer> activeWinLines, List<Integer> activeReels) {

        List<WinLine> winLines = new ArrayList<>();
        if (gameConfig.isWineLineOnlyOnAllActiveReels()) {
            for (Integer activeWinLine : activeWinLines) {
                for (WinLine winLine : gameConfig.getWinLines()) {
                    if (winLine.getIndex() == activeWinLine && winLine.getReels() == activeReels.size()) {
                        winLines.add(winLine);
                    }
                }
            }
            return winLines;
        } else {
            for (Integer activeWinLine : activeWinLines) {
                for (WinLine winLine : gameConfig.getWinLines()) {
                    if (winLine.getIndex() == activeWinLine && winLine.getReels() <= activeReels.size()) {
                        winLines.add(winLine);
                    }
                }
            }
            return winLines;
        }
    }

    private void filterOnlyHighestResultsInWinLine(List<ResultWin> resultWins) {
        List<ResultWin> resultWinsTemp;
        List<ResultWin> resultsWithSizeFive = new ArrayList<>();
        for (ResultWin resultWin : resultWins) {
            if (resultWin.getIndexes().size() == 5) {
                resultsWithSizeFive.add(resultWin);
            }
        }
        resultWinsTemp = new ArrayList<>(resultWins);
        for (ResultWin resultSize5 : resultsWithSizeFive) {
            for (ResultWin resultWin : resultWinsTemp) {
                if (resultWin.getIndexes().size() < 5 && resultSize5.getIndexes().containsAll(resultWin.getIndexes())) {
                    resultWins.remove(resultWin);
                }
            }
        }
        List<ResultWin> resultsWithSizeFour = new ArrayList<>();
        for (ResultWin resultWin : resultWins) {
            if (resultWin.getIndexes().size() == 4) {
                resultsWithSizeFour.add(resultWin);
            }
        }
        resultWinsTemp = new ArrayList<>(resultWins);
        for (ResultWin resultSize5 : resultsWithSizeFour) {
            for (ResultWin resultWin : resultWinsTemp) {
                if (resultWin.getIndexes().size() < 4 && resultSize5.getIndexes().containsAll(resultWin.getIndexes())) {
                    resultWins.remove(resultWin);
                }
            }
        }
    }

    private List<Integer> collectListFromIndexes(List<Integer> indexes, List<Integer> list) {
        List<Integer> results = new ArrayList<>();
        for (Integer index : indexes) {
            results.add(list.get(index));
        }
        return results;
    }


    private boolean compareEqualityOfNumbers(int index, List<Integer> numbers) {
        for (int i = 0; i < numbers.size(); i++) {
            int check = (index ^ numbers.get(i));
            if (check != 0) {
                return false;
            }
        }
        return true;
    }

    private List<Integer> createSymbolsToOneArray(List<List<Integer>> symbols) {
        List<Integer> elements = new ArrayList<>();
        for (List<Integer> symbol : symbols) {
            for (Integer integer : symbol) {
                elements.add(integer);
            }
        }
        return elements;
    }
}


