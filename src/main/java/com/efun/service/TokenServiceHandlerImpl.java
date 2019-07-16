package com.efun.service;

import com.efun.message.MessageGameStart;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.commons.codec.digest.DigestUtils;

@Service
public class TokenServiceHandlerImpl implements TokenServiceHandler {

    private Set<String> tokens = new CopyOnWriteArraySet<>();

    /**
     * Method generating authorization token using md5 algorithm
     * using date and size of sessions
     *
     * @author Michał Siwiak
     * @param Map<String, MessageGameStart> sessions - map holding current sessions
     * @return String - authorization token
     *
     */
    @Override
    public String generateToken(Map<String, MessageGameStart> sessions) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String token = simpleDateFormat.format(new Date()) + sessions.size();
        String md5Hex = DigestUtils.md5Hex(token).toUpperCase();
        tokens.add(md5Hex);
        return md5Hex;
    }

    /**
     * Method to authorize request by checking token in set.
     *
     * @author Michał Siwiak
     * @param String token
     * @return boolean true when authorization is positive or false when not
     *
     */

    @Override
    public boolean authorizeRequest(String token) {
        return tokens.contains(token);
    }


    /**
     * Method removing token when game is closed
     *
     * @author Michał Siwiak
     * @param String token
     *
     */
    @Override
    public void removeToken(String token) {
        tokens.remove(token);
    }
}