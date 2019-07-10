package com.efun.message;

import java.util.List;

public class InitParams {

    List<Integer> winLinesSelected;
    List<Integer> reelsSelected;

    public List<Integer> getWinLinesSelected() {
        return winLinesSelected;
    }

    public void setWinLinesSelected(List<Integer> winLinesSelected) {
        this.winLinesSelected = winLinesSelected;
    }

    public List<Integer> getReelsSelected() {
        return reelsSelected;
    }

    public void setReelsSelected(List<Integer> reelsSelected) {
        this.reelsSelected = reelsSelected;
    }

    @Override
    public String toString() {
        return "InitParams{" +
                "winLinesSelected=" + winLinesSelected +
                ", reelsSelected=" + reelsSelected +
                '}';
    }
}
