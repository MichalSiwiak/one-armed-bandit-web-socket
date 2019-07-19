package com.efun.components;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class Pocket {

    private String gameId;
    private BigDecimal balance = new BigDecimal("5000");

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
