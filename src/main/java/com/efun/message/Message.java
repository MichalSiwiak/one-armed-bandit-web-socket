package com.efun.message;

import com.efun.components.WinLineData;
import com.efun.constants.Status;

import java.math.BigDecimal;
import java.util.List;

public abstract class Message {

    private Status status;
    private String gameId;
    private String authorizationToken;
    private int rno;
    private String message;
    private WinLineData winLineData;
    private List<List<Integer>> symbols;
    private BigDecimal winValue;
    private BigDecimal balance;
    private List<Integer> activeWinLines;
    private List<Integer> activeReels;
    private boolean win;

    @Override
    public String toString() {
        return "Message{" +
                "status=" + status +
                ", gameId='" + gameId + '\'' +
                ", authorizationToken='" + authorizationToken + '\'' +
                ", rno=" + rno +
                ", message='" + message + '\'' +
                ", winLineData=" + winLineData +
                ", symbols=" + symbols +
                ", winValue=" + winValue +
                ", balance=" + balance +
                ", activeWinLines=" + activeWinLines +
                ", activeReels=" + activeReels +
                ", win=" + win +
                '}';
    }

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

    public String getAuthorizationToken() {
        return authorizationToken;
    }

    public void setAuthorizationToken(String authorizationToken) {
        this.authorizationToken = authorizationToken;
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

    public WinLineData getWinLineData() {
        return winLineData;
    }

    public void setWinLineData(WinLineData winLineData) {
        this.winLineData = winLineData;
    }

    public List<List<Integer>> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<List<Integer>> symbols) {
        this.symbols = symbols;
    }

    public BigDecimal getWinValue() {
        return winValue;
    }

    public void setWinValue(BigDecimal winValue) {
        this.winValue = winValue;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<Integer> getActiveWinLines() {
        return activeWinLines;
    }

    public void setActiveWinLines(List<Integer> activeWinLines) {
        this.activeWinLines = activeWinLines;
    }

    public List<Integer> getActiveReels() {
        return activeReels;
    }

    public void setActiveReels(List<Integer> activeReels) {
        this.activeReels = activeReels;
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }
}

