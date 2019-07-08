package com.efun.model;

import java.util.List;

public class WinlinesData {

    private double quantity;
    private List<WinLine> winLines;

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public List<WinLine> getWinLines() {
        return winLines;
    }

    public void setWinLines(List<WinLine> winLines) {
        this.winLines = winLines;
    }
}