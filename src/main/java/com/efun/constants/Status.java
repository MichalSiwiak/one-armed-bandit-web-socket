package com.efun.constants;


/**
 * Class representing application statuses:
 * @author Michał Siwiak
 *
 * NEW: when game was created successfully without any errors.
 * ACTIVE: when game is active and client send at least one spin.
 * TERMINATED: when games was closed.
 * LIMIT_REACHED: when limit of creating games was reached.
 * This the limit is determined in configuration file: max_game_number
 *
 * CONFIGURATION_NOT_ACCEPTED: when game have incorrect configuration
 * and win is not possible in each spin.
 *
 * UNAUTHORIZED when client cannot execute spin or end game because of lack of authorization.
 * In this application implementation of authorization is very simple: keys are holding
 * in HashSet of strings
 *
 */
public enum Status {

    NEW, ACTIVE, TERMINATED, LIMIT_REACHED, CONFIGURATION_NOT_ACCEPTED, UNAUTHORIZED
}
