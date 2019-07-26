package com.efun.components;

import com.efun.entity.CombinationResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class SimulationReportEnd {

    private int numberOfSpins;
    private int numberOfWins;
    private BigDecimal bet;
    private BigDecimal sumOfWins;
    private BigDecimal valueOfWins;
    private BigDecimal startingBalance;
    private BigDecimal endBalance;
    private Set<Integer> indexesWins;
    private List<CombinationResult> combinationResults; //zrobić konstruktor klasy w oparciu o listę !!!!
    //private Set<WinLine> winLineWins;


    @Override
    public String toString() {
        return "SimulationReportEnd{" +
                "numberOfSpins=" + numberOfSpins +
                ", numberOfWins=" + numberOfWins +
                ", bet=" + bet +
                ", sumOfWins=" + sumOfWins +
                ", valueOfWins=" + valueOfWins +
                ", startingBalance=" + startingBalance +
                ", endBalance=" + endBalance +
                ", indexesWins=" + indexesWins +
                '}';
    }

    public List<CombinationResult> getCombinationResults() {
        return combinationResults;
    }

    public void setCombinationResults(List<CombinationResult> combinationResults) {
        this.combinationResults = combinationResults;
    }

    public int getNumberOfSpins() {
        return numberOfSpins;
    }

    public void setNumberOfSpins(int numberOfSpins) {
        this.numberOfSpins = numberOfSpins;
    }

    public int getNumberOfWins() {
        return numberOfWins;
    }

    public void setNumberOfWins(int numberOfWins) {
        this.numberOfWins = numberOfWins;
    }

    public BigDecimal getBet() {
        return bet;
    }

    public void setBet(BigDecimal bet) {
        this.bet = bet;
    }

    public BigDecimal getSumOfWins() {
        return sumOfWins;
    }

    public void setSumOfWins(BigDecimal sumOfWins) {
        this.sumOfWins = sumOfWins;
    }

    public BigDecimal getValueOfWins() {
        return valueOfWins;
    }

    public void setValueOfWins(BigDecimal valueOfWins) {
        this.valueOfWins = valueOfWins;
    }

    public BigDecimal getStartingBalance() {
        return startingBalance;
    }

    public void setStartingBalance(BigDecimal startingBalance) {
        this.startingBalance = startingBalance;
    }

    public BigDecimal getEndBalance() {
        return endBalance;
    }

    public void setEndBalance(BigDecimal endBalance) {
        this.endBalance = endBalance;
    }

    public Set<Integer> getIndexesWins() {
        return indexesWins;
    }

    public void setIndexesWins(Set<Integer> indexesWins) {
        this.indexesWins = indexesWins;
    }
}