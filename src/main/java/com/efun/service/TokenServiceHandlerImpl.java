package com.efun.service;

import com.efun.message.Message;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.codec.digest.DigestUtils;

@Service
public class TokenServiceHandlerImpl implements TokenServiceHandler {

    private Map<String, String> tokens = new ConcurrentHashMap<>();

    /**
     * Method generating authorization token using md5 algorithm
     * using date and size of sessions
     *
     * @param Map<String, MessageGameStart> sessions - map holding current sessions
     * @return String - authorization token
     * @author Michał Siwiak
     */
    @Override
    public String generateToken(String gameId, Map<String, Message> sessions) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String token = simpleDateFormat.format(new Date()) + sessions.size();
        String md5Hex = DigestUtils.md5Hex(token).toUpperCase();
        tokens.put(md5Hex, gameId);
        return md5Hex;
    }

    /**
     * Method to authorize request by checking token in set.
     *
     * @param String token
     * @return boolean true when authorization is positive or false when not
     * @author Michał Siwiak
     */

    @Override
    public boolean authorizeRequest(String gameId, String token, Map<String, Message> sessions) {
        if (tokens.containsKey(token)) {
            if (sessions.get(token).getGameId().equals(gameId)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Method removing token when game is closed
     *
     * @param String token
     * @author Michał Siwiak
     */
    @Override
    public void removeToken(String token) {
        tokens.remove(token);
    }
}