package com.efun.model;

import java.util.List;
import java.util.Objects;

// class represents one result and positions of reels in Random Numbers (rno)
public class RandomNumberResult {

    int randomNumber;
    private List<List<Byte>> reelsInRandomNumber;
    boolean win;

    public int getRandomNumber() {
        return randomNumber;
    }

    public void setRandomNumber(int randomNumber) {
        this.randomNumber = randomNumber;
    }

    public List<List<Byte>> getReelsInRandomNumber() {
        return reelsInRandomNumber;
    }

    public void setReelsInRandomNumber(List<List<Byte>> reelsInRandomNumber) {
        this.reelsInRandomNumber = reelsInRandomNumber;
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    @Override
    public String toString() {
        return "RandomNumberResult{" +
                "randomNumber=" + randomNumber +
                ", reelsInRandomNumber=" + reelsInRandomNumber +
                ", win=" + win +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RandomNumberResult that = (RandomNumberResult) o;
        return randomNumber == that.randomNumber &&
                win == that.win &&
                Objects.equals(reelsInRandomNumber, that.reelsInRandomNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(randomNumber, reelsInRandomNumber, win);
    }
}
