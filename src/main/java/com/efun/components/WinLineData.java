package com.efun.components;

import com.efun.config.WinLine;

import java.util.List;

/**
 * Auxiliary  Class represents win line data.
 * @author Michał Siwiak
 */
public class WinLineData {

    private int quantity;
    private List<WinLine> winLineList;

    public WinLineData(List<WinLine> winLineList) {
        this.quantity = winLineList.size();
        this.winLineList = winLineList;
    }

    public void setWinLineList(List<WinLine> winLineList) {
        this.winLineList = winLineList;
    }

    public int getQuantity() {
        return quantity;
    }

    public List<WinLine> getWinLineList() {
        return winLineList;
    }

    @Override
    public String toString() {
        return "WinLineData{" +
                "quantity=" + quantity +
                ", winLineList=" + winLineList +
                '}';
    }
}