package com.efun.entity;

import com.efun.components.ResultWin;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

public class CombinationResult {

    @Id
    private String id;
    private String combinationId;
    private List<Integer> combinationArray;
    private int rno;
    private List<List<Integer>> symbols;
    private boolean isWin;
    private List<Integer> numbersOfWins = new ArrayList<>();
    private List<ResultWin> resultWinList;

    public CombinationResult(String combinationId, List<Integer> combinationArray, int rno, List<ResultWin> resultWinList) {

        this.combinationId = combinationId;
        this.combinationArray = combinationArray;
        this.rno = rno;
        this.resultWinList = resultWinList;

        if (resultWinList.size() == 0) {
            this.isWin = false;
        } else {
            this.isWin = true;
            for (ResultWin resultWin : resultWinList) {
                numbersOfWins.add(resultWin.getEqualValues().iterator().next());
            }
        }
    }

    @Override
    public String toString() {
        return "CombinationResult{" +
                "id='" + id + '\'' +
                ", combinationId='" + combinationId + '\'' +
                ", combinationArray=" + combinationArray +
                ", rno=" + rno +
                ", symbols=" + symbols +
                ", isWin=" + isWin +
                ", numbersOfWins=" + numbersOfWins +
                ", resultWinList=" + resultWinList +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCombinationId() {
        return combinationId;
    }

    public void setCombinationId(String combinationId) {
        this.combinationId = combinationId;
    }

    public List<Integer> getCombinationArray() {
        return combinationArray;
    }

    public void setCombinationArray(List<Integer> combinationArray) {
        this.combinationArray = combinationArray;
    }

    public int getRno() {
        return rno;
    }

    public void setRno(int rno) {
        this.rno = rno;
    }

    public boolean isWin() {
        return isWin;
    }

    public void setWin(boolean win) {
        isWin = win;
    }

    public List<Integer> getNumbersOfWins() {
        return numbersOfWins;
    }

    public void setNumbersOfWins(List<Integer> numbersOfWins) {
        this.numbersOfWins = numbersOfWins;
    }

    public List<ResultWin> getResultWinList() {
        return resultWinList;
    }

    public void setResultWinList(List<ResultWin> resultWinList) {
        this.resultWinList = resultWinList;
    }

    public List<List<Integer>> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<List<Integer>> symbols) {
        this.symbols = symbols;
    }
}
