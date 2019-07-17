package com.efun.message;

/**
 * Class represents parameters when client want to execute spin.
 * @author Michał Siwiak
 */
public class EndParams {

    private String authorizationToken;
    private String gameId;

    @Override
    public String toString() {
        return "EndParams{" +
                "authorizationToken='" + authorizationToken + '\'' +
                ", gameId='" + gameId + '\'' +
                '}';
    }

    public String getAuthorizationToken() {
        return authorizationToken;
    }

    public void setAuthorizationToken(String authorizationToken) {
        this.authorizationToken = authorizationToken;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
