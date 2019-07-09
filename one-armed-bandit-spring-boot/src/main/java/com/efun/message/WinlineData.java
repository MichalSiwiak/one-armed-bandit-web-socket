package com.efun.message;

import java.util.List;

public class WinlineData {

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

    @Override
    public String toString() {
        return "WinlineData{" +
                "quantity=" + quantity +
                ", winLines=" + winLines +
                '}';
    }
}