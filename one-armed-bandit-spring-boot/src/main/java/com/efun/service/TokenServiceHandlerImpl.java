package com.efun.service;

import com.efun.message.MessageGameStart;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.codec.digest.DigestUtils;

@Service
public class TokenServiceHandlerImpl implements TokenServiceHandler {

    private Set<String> tokens = new HashSet<>();

    @Override
    public String generateToken(Map<String, MessageGameStart> sessions) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String token = simpleDateFormat.format(new Date()) + sessions.size();
        String md5Hex = DigestUtils.md5Hex(token).toUpperCase();
        tokens.add(md5Hex);
        return md5Hex;
    }

    @Override
    public boolean authorizeRequest(String token) {
        return tokens.contains(token);
    }

    @Override
    public void removeToken(String token) {
        tokens.remove(token);
    }
}
