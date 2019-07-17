package com.efun.message;

/**
 * Class represents parameters when client want to execute spin.
 * @author Michał Siwiak
 */
public class SpinParams {

    private String rno;
    private String bet;
    private String authorizationToken;
    private String gameId;

    public String getRno() {
        return rno;
    }

    public void setRno(String rno) {
        this.rno = rno;
    }

    public String getBet() {
        return bet;
    }

    public void setBet(String bet) {
        this.bet = bet;
    }

    public String getAuthorizationToken() {
        return authorizationToken;
    }

    public void setAuthorizationToken(String authorizationToken) {
        this.authorizationToken = authorizationToken;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    @Override
    public String toString() {
        return "SpinParams{" +
                "rno=" + rno +
                ", bet='" + bet + '\'' +
                ", authorizationToken='" + authorizationToken + '\'' +
                ", gameId='" + gameId + '\'' +
                '}';
    }
}
