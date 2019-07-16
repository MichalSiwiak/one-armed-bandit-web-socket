package com.efun.message;

import com.efun.constants.Status;

/**
 * Class represents message when client creates game
 *
 */
public class MessageGameStart {

    private Status status;//
    private String gameId;//
    private String authorizationToken;
    private int rno;//
    private String message;
    private WinLineData winlineData;

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

    public WinLineData getWinlineData() {
        return winlineData;
    }

    public void setWinlineData(WinLineData winlineData) {
        this.winlineData = winlineData;
    }

    @Override
    public String toString() {
        return "MessageGameStart{" +
                "status='" + status + '\'' +
                ", gameId='" + gameId + '\'' +
                ", authorizationToken='" + authorizationToken + '\'' +
                ", rno=" + rno +
                ", message='" + message + '\'' +
                ", winlineData=" + winlineData +
                '}';
    }
}
