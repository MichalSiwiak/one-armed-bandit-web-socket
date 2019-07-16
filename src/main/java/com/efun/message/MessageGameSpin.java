package com.efun.message;

import com.efun.constants.Status;

import java.util.List;

/**
 * Class represents message when client executes spins.
 * @author Michał Siwiak
 */
public class MessageGameSpin {

    private Status status;
    private String gameId;
    private int rno;
    private String message;
    private WinLineData winlineData;
    private List<List<Integer>> symbols;
    private double win;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
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

    public WinLineData getWinlineData() {
        return winlineData;
    }

    public void setWinlineData(WinLineData winlineData) {
        this.winlineData = winlineData;
    }

    public List<List<Integer>> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<List<Integer>> symbols) {
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
                ", gameId='" + gameId + '\'' +
                ", rno=" + rno +
                ", message='" + message + '\'' +
                ", winlineData=" + winlineData +
                ", symbols=" + symbols +
                ", win=" + win +
                '}';
    }
}
