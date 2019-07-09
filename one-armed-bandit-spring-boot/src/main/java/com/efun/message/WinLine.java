package com.efun.message;

import java.util.List;

public class WinLine {
    private int index;
    private List<Integer> positions;
    private double multiply;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<Integer> getPositions() {
        return positions;
    }

    public void setPositions(List<Integer> positions) {
        this.positions = positions;
    }

    public double getMultiply() {
        return multiply;
    }

    public void setMultiply(double multiply) {
        this.multiply = multiply;
    }

    @Override
    public String toString() {
        return "WinLine{" +
                "index=" + index +
                ", positions=" + positions +
                ", multiply=" + multiply +
                '}';
    }
}