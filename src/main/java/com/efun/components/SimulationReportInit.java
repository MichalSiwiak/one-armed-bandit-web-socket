package com.efun.components;

import java.math.BigDecimal;
import java.util.List;

public class SimulationReportInit {

    private int start;
    private int size;
    private List<Integer> activeReels;
    private List<Integer> activeWinLines;
    private BigDecimal startingBalance;
    private BigDecimal bet;

    public BigDecimal getBet() {
        return bet;
    }

    public void setBet(BigDecimal bet) {
        this.bet = bet;
    }

    public BigDecimal getStartingBalance() {
        return startingBalance;
    }

    public void setStartingBalance(BigDecimal startingBalance) {
        this.startingBalance = startingBalance;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<Integer> getActiveReels() {
        return activeReels;
    }

    public void setActiveReels(List<Integer> activeReels) {
        this.activeReels = activeReels;
    }

    public List<Integer> getActiveWinLines() {
        return activeWinLines;
    }

    public void setActiveWinLines(List<Integer> activeWinLines) {
        this.activeWinLines = activeWinLines;
    }
}