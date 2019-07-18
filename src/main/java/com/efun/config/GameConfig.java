package com.efun.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Class represents configuration file
 * @author Michał Siwiak
 *
 */
@Component
@ConfigurationProperties("game-config")
public class GameConfig {

    private List<List<Integer>> reels = new ArrayList<>();
    private List<Integer> spin = new ArrayList<>();
    private List<Double> winnings = new ArrayList<>();
    private List<List<Integer>> wins = new ArrayList<>();

    public List<List<Integer>> getReels() {
        return reels;
    }

    public void setReels(List<List<Integer>> reels) {
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

    public List<List<Integer>> getWins() {
        return wins;
    }

    public void setWins(List<List<Integer>> wins) {
        this.wins = wins;
    }
}