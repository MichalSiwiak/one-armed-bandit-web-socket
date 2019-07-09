package com.efun.message;

public class MessageGameStart {

    private String status;//
    private int gameId;//
    private String authorizationToken;
    private int rno;//
    private String message;
    private WinlineData winlineData;

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

    public WinlineData getWinlineData() {
        return winlineData;
    }

    public void setWinlineData(WinlineData winlineData) {
        this.winlineData = winlineData;
    }

    @Override
    public String toString() {
        return "MessageGameStart{" +
                "status='" + status + '\'' +
                ", gameId=" + gameId +
                ", authorizationToken='" + authorizationToken + '\'' +
                ", rno=" + rno +
                ", message='" + message + '\'' +
                ", winlineData=" + winlineData +
                '}';
    }
}
