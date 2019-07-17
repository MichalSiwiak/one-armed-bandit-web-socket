package com.efun.message;

import com.efun.constants.Status;

/**
 * Class represents message when client ends the game.
 * @author Michał Siwiak
 */
public class MessageGameError extends Message{

    public MessageGameError(Status status) {
        setStatus(status);
        setMessage(super.getMessage());
    }
}
