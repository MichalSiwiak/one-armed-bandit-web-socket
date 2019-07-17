package com.efun.service;

import com.efun.message.Message;
import com.efun.message.MessageGameStart;

import java.util.Map;

public interface TokenServiceHandler {

    public String generateToken(String gameId, Map<String, Message> sessions);

    public boolean authorizeRequest(String gameId, String token, Map<String, Message> sessions);

    public void removeToken(String token);

}