package com.efun.components;

import java.util.List;
import java.util.Objects;

public class ResultWin {

    private int index;
    private int multiply;
    private int reels;
    private List<Integer> equalValues;
    private List<Integer> indexes;

    public ResultWin(int index, int multiply, int reels, List<Integer> equalValues, List<Integer> indexes) {
        this.index = index;
        this.multiply = multiply;
        this.reels = reels;
        this.equalValues = equalValues;
        this.indexes = indexes;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getMultiply() {
        return multiply;
    }

    public void setMultiply(int multiply) {
        this.multiply = multiply;
    }

    public int getReels() {
        return reels;
    }

    public void setReels(int reels) {
        this.reels = reels;
    }

    public List<Integer> getEqualValues() {
        return equalValues;
    }

    public void setEqualValues(List<Integer> equalValues) {
        this.equalValues = equalValues;
    }

    public List<Integer> getIndexes() {
        return indexes;
    }

    public void setIndexes(List<Integer> indexes) {
        this.indexes = indexes;
    }

    @Override
    public String toString() {
        return "ResultWin{" +
                "index=" + index +
                ", multiply=" + multiply +
                ", reels=" + reels +
                ", equalValues=" + equalValues +
                ", indexes=" + indexes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultWin resultWin = (ResultWin) o;
        return index == resultWin.index &&
                multiply == resultWin.multiply &&
                reels == resultWin.reels &&
                Objects.equals(equalValues, resultWin.equalValues) &&
                Objects.equals(indexes, resultWin.indexes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, multiply, reels, equalValues, indexes);
    }
}
