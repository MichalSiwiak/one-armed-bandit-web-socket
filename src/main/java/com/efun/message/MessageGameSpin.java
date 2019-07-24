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
        setWinLineData(super.getWinLineData());
        setSymbols(super.getSymbols());
        setWinValue(super.getWinValue());
        setWin(super.isWin());
        setMessage(super.getMessage());
        setBalance(super.getBalance());
        setTotalWinInSpin(super.getTotalWinInSpin());
    }
}