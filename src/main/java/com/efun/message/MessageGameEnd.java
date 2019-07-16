package com.efun.message;

import com.efun.constants.Status;

/**
 * Class represents message when client ends the game.
 *
 */
public class MessageGameEnd {

    private Status status;
    private String gameId;
    private int rno;
    private String message;

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
