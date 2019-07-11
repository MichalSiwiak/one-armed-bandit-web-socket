package com.efun.message;

import java.util.List;

public class InitParams {

    List<Integer> winLinesSelected;
    List<Integer> reelsSelected;

    public InitParams(List<Integer> winLinesSelected, List<Integer> reelsSelected) {
        this.winLinesSelected = winLinesSelected;
        this.reelsSelected = reelsSelected;
    }

    public List<Integer> getWinLinesSelected() {
        return winLinesSelected;
    }

    public List<Integer> getReelsSelected() {
        return reelsSelected;
    }
}
