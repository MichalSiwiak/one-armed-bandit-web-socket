package com.efun.config;

import java.util.List;

//class represents configuration file
public class GameConfig {

    private List<List<Byte>> reels;
    private List<Integer> spin;
    private List<Double> winnings;

    public List<List<Byte>> getReels() {
        return reels;
    }

    public void setReels(List<List<Byte>> reels) {
        this.reels = reels;
    }

    public List<Integer> getSpin() {
        return spin;
    }

    public void setSpin(List<Integer> spin) {
        this.spin = spin;
    }

    public List<Double> getWinnings() {
        return winnings;
    }

    public void setWinnings(List<Double> winnings) {
        this.winnings = winnings;
    }

    @Override
    public String toString() {
        return "GameConfig{" +
                "reels=" + reels +
                ", spin=" + spin +
                ", winnings=" + winnings +
                '}';
    }
}