package com.efun.message;

/**
 * Class represents parameters when client want to execute spin.
 * @author Michał Siwiak
 */
public class SpinParams {

    private int rno;
    private int bet;
    private String authorizationToken;

    public int getRno() {
        return rno;
    }

    public void setRno(int rno) {
        this.rno = rno;
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public String getAuthorizationToken() {
        return authorizationToken;
    }

    public void setAuthorizationToken(String authorizationToken) {
        this.authorizationToken = authorizationToken;
    }

    @Override
    public String toString() {
        return "SpinParams{" +
                "rno=" + rno +
                ", bet=" + bet +
                ", authorizationToken='" + authorizationToken + '\'' +
                '}';
    }
}
