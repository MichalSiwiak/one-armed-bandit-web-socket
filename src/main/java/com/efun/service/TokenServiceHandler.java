package com.efun.service;

import com.efun.message.MessageGameStart;
import java.util.Map;

public interface TokenServiceHandler {

    public String generateToken(Map<String, MessageGameStart> sessions);
    public boolean authorizeRequest(String token);
    public void removeToken(String token);

}
