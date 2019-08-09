package com.efun.message;

import com.efun.constants.Status;
import org.springframework.stereotype.Component;

@Component
public class MessageFactory {

    public Message createMessage(Status status) {
        if (status.equals(Status.NEW)) {
            return new MessageGameStart(status);
        } else if (status.equals(Status.ACTIVE)) {
            return new MessageGameSpin(status);
        } else if (status.equals(Status.TERMINATED)) {
            return new MessageGameEnd(status);
        } else if (status.equals(Status.LIMIT_REACHED)
                || status.equals(Status.UNAUTHORIZED)
                || status.equals(Status.CONFIGURATION_NOT_ACCEPTED)
                || status.equals(Status.SERVER_BUSY)
                || status.equals(Status.INCORRECT_DATA)) {
            return new MessageGameError(status);
        } else return null;
    }
}