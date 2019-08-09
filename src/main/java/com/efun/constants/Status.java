package com.efun.constants;


/**
 * Class representing application statuses:
 *
 * @author Michał Siwiak
 * <p>
 * NEW: when game was created successfully without any errors.
 * ACTIVE: when game is active and client send at least one spin.
 * TERMINATED: when games was closed.
 * LIMIT_REACHED: when limit of creating games was reached.
 * This the limit is determined in configuration file: max_game_number
 * <p>
 * CONFIGURATION_NOT_ACCEPTED: when game have incorrect configuration
 * and win is not possible in each spin.
 * <p>
 * UNAUTHORIZED when client cannot execute spin or end game because of lack of authorization.
 * In this application implementation of authorization is very simple: keys are holding
 * in HashSet of strings
 */
public enum Status {

    NEW("Game configured successfully"),
    ACTIVE("Spin executed successfully"),
    TERMINATED("The game was closed successfully"),
    LIMIT_REACHED("Maximum number of games has been exceeded"),
    CONFIGURATION_NOT_ACCEPTED("No wins calculated for this configuration"),
    UNAUTHORIZED("Unknown authorization of game"),
    INCORRECT_DATA("Incorrect data has been sent"),
    SERVER_BUSY("Cannot start game now because game config is now updated. Please wait few seconds.");


    private String messageBody;

    Status(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getMessageBody() {
        return messageBody;
    }
}
