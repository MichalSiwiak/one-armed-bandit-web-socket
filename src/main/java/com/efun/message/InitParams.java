package com.efun.message;

import java.util.List;


/**
 * Class represents initial parameters when client is starting the game
 * Client decides which win lines reels can guarantee win and reels.
 * This application supports only exact 3 reels to creating game.
 *
 */
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
