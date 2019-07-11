package com.efun.message;

public class MessageGameEnd {

    private String status;
    private String gameId;
    private int rno;
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    @Override
    public String toString() {
        return "MessageGameEnd{" +
                "status='" + status + '\'' +
                ", gameId=" + gameId +
                ", rno=" + rno +
                ", message='" + message + '\'' +
                '}';
    }
}
