package com.efun.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WinLine {
    private int index;
    private int reels;
    private List<List<Integer>> positions = new ArrayList<>();
    private int multiply;

    @Override
    public String toString() {
        return "WinLine{" +
                "index=" + index +
                ", reels=" + reels +
                ", positions=" + positions +
                ", multiply=" + multiply +
                '}';
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getReels() {
        return reels;
    }

    public void setReels(int reels) {
        this.reels = reels;
    }

    public List<List<Integer>> getPositions() {
        return positions;
    }

    public void setPositions(List<List<Integer>> positions) {
        this.positions = positions;
    }

    public int getMultiply() {
        return multiply;
    }

    public void setMultiply(int multiply) {
        this.multiply = multiply;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WinLine)) return false;
        WinLine winLine = (WinLine) o;
        return getIndex() == winLine.getIndex() &&
                getReels() == winLine.getReels() &&
                getMultiply() == winLine.getMultiply() &&
                Objects.equals(getPositions(), winLine.getPositions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIndex(), getReels(), getPositions(), getMultiply());
    }
}