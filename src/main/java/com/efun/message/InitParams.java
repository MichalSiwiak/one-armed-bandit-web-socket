package com.efun.message;

import java.util.List;


/**
 * Class represents initial parameters when client is starting the game
 * Client decides which win lines reels can guarantee win and reels.
 * This application supports only exact 3 reels to creating game.
 * @author Michał Siwiak
 *
 */
public class InitParams {

    private List<Integer> winLinesSelected;
    private List<Integer> reelsSelected;

    @Override
    public String toString() {
        return "InitParams{" +
                "winLinesSelected=" + winLinesSelected +
                ", reelsSelected=" + reelsSelected +
                '}';
    }

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
}
