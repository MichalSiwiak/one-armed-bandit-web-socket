package com.efun.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class represents configuration file
 *
 * @author Michał Siwiak
 */
/*@Component
@ConfigurationProperties("game-config")*/
public class GameConfig {

    private List<List<Integer>> reels = new ArrayList<>();
    private List<Integer> spin = new ArrayList<>();
    private List<Double> winnings = new ArrayList<>();
    private List<WinLine> winLines = new ArrayList<>();
    private boolean wineLineOnlyOnAllActiveReels;
    private boolean filterOnlyHighestResultsInWinLine;

    @Override
    public String toString() {
        return "GameConfig{" +
                "reels=" + reels +
                ", spin=" + spin +
                ", winnings=" + winnings +
                ", winLines=" + winLines +
                ", wineLineOnlyOnAllActiveReels=" + wineLineOnlyOnAllActiveReels +
                ", filterOnlyHighestResultsInWinLine=" + filterOnlyHighestResultsInWinLine +
                '}';
    }

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

    public List<WinLine> getWinLines() {
        return winLines;
    }

    public void setWinLines(List<WinLine> winLines) {
        this.winLines = winLines;
    }

    public boolean isWineLineOnlyOnAllActiveReels() {
        return wineLineOnlyOnAllActiveReels;
    }

    public void setWineLineOnlyOnAllActiveReels(boolean wineLineOnlyOnAllActiveReels) {
        this.wineLineOnlyOnAllActiveReels = wineLineOnlyOnAllActiveReels;
    }

    public boolean isFilterOnlyHighestResultsInWinLine() {
        return filterOnlyHighestResultsInWinLine;
    }

    public void setFilterOnlyHighestResultsInWinLine(boolean filterOnlyHighestResultsInWinLine) {
        this.filterOnlyHighestResultsInWinLine = filterOnlyHighestResultsInWinLine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameConfig)) return false;
        GameConfig that = (GameConfig) o;
        return isWineLineOnlyOnAllActiveReels() == that.isWineLineOnlyOnAllActiveReels() &&
                isFilterOnlyHighestResultsInWinLine() == that.isFilterOnlyHighestResultsInWinLine() &&
                Objects.equals(getReels(), that.getReels()) &&
                Objects.equals(getSpin(), that.getSpin()) &&
                Objects.equals(getWinnings(), that.getWinnings()) &&
                Objects.equals(getWinLines(), that.getWinLines());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getReels(), getSpin(), getWinnings(), getWinLines(), isWineLineOnlyOnAllActiveReels(), isFilterOnlyHighestResultsInWinLine());
    }
}