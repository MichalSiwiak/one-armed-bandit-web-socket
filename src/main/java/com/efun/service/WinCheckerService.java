package com.efun.service;

import com.efun.components.TotalWinInSpin;
import com.efun.config.WinLine;
import java.util.List;

public interface WinCheckerService {


    public TotalWinInSpin getWins(List<List<Integer>> symbols, List<Integer> activeReels, List<Integer> activeWinLines);

    public boolean isWin(List<List<Integer>> symbols, List<Integer> activeReels, List<Integer> activeWinLines);

    public List<WinLine> getWinLinesData(List<Integer> activeWinLines, List<Integer> activeReels);
}