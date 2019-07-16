package com.efun.entity;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * Class represents one result and positions of reels in Random Numbers (rno)
 * This is temporary creating in some database when client started game
 * and and it is removed when game is closed.
 */
@Component
public class RandomNumberResult {

    private ObjectId id;
    private int randomNumber;
    private List<List<Integer>> reelsInRandomNumber;
    private boolean win;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public int getRandomNumber() {
        return randomNumber;
    }

    public void setRandomNumber(int randomNumber) {
        this.randomNumber = randomNumber;
    }

    public List<List<Integer>> getReelsInRandomNumber() {
        return reelsInRandomNumber;
    }

    public void setReelsInRandomNumber(List<List<Integer>> reelsInRandomNumber) {
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
                "id=" + id +
                ", randomNumber=" + randomNumber +
                ", reelsInRandomNumber=" + reelsInRandomNumber +
                ", win=" + win +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RandomNumberResult that = (RandomNumberResult) o;
        return randomNumber == that.randomNumber &&
                win == that.win &&
                Objects.equals(id, that.id) &&
                Objects.equals(reelsInRandomNumber, that.reelsInRandomNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, randomNumber, reelsInRandomNumber, win);
    }
}
