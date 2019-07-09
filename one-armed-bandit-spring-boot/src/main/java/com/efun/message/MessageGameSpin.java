package com.efun.message;

import java.util.List;

public class MessageGameSpin {

    private String status;
    private int gameId;
    private int rno;
    private String message;
    private WinlineData winlineData;
    private List<List<Byte>> symbols;
    private double win;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getRno() {
        return rno;
    }

    public void setRno(int rno) {
        this.rno = rno;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public WinlineData getWinlineData() {
        return winlineData;
    }

    public void setWinlineData(WinlineData winlineData) {
        this.winlineData = winlineData;
    }

    public List<List<Byte>> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<List<Byte>> symbols) {
        this.symbols = symbols;
    }

    public double getWin() {
        return win;
    }

    public void setWin(double win) {
        this.win = win;
    }

    @Override
    public String toString() {
        return "MessageGameSpin{" +
                "status='" + status + '\'' +
                ", gameId=" + gameId +
                ", rno=" + rno +
                ", message='" + message + '\'' +
                ", winlineData=" + winlineData +
                ", symbols=" + symbols +
                ", win=" + win +
                '}';
    }
}
