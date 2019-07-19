package com.efun.message;

import com.efun.constants.Status;

/**
 * Class represents message when client executes spins.
 *
 * @author Michał Siwiak
 */
public class MessageGameSpin extends Message {

    public MessageGameSpin(Status status) {
        setStatus(status);
        setGameId(super.getGameId());
        setRno(super.getRno());
        setWinlineData(super.getWinlineData());
        setSymbols(super.getSymbols());
        setWin(super.getWin());
        setMessage(super.getMessage());
        setBalance(super.getBalance());
    }
}