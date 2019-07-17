package com.efun.message;

import com.efun.constants.Status;

/**
 * Class represents message when client creates game
 *
 * @author Michał Siwiak
 */
public class MessageGameStart extends Message {

    public MessageGameStart(Status status) {
        setStatus(status);
        setGameId(super.getGameId());
        setAuthorizationToken(super.getAuthorizationToken());
        setRno(super.getRno());
        setWinlineData(super.getWinlineData());
        setMessage(super.getMessage());
    }
}
