package com.efun.message;

import com.efun.constants.Status;

/**
 * Class represents message when client ends the game.
 * @author Michał Siwiak
 */
public class MessageGameEnd extends Message{

    public MessageGameEnd(Status status) {
        setStatus(status);
        setGameId(super.getGameId());
        setRno(super.getRno());
        setMessage(super.getMessage());
        setBalance(super.getBalance());
        setWin(super.isWin());
        setWinValue(super.getWinValue());
    }
}
