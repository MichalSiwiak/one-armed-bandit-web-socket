package com.efun.service;

import com.efun.message.*;

import java.util.List;

public interface MessageProviderService {

    public Message startGame(List<Integer> winLines, List<Integer> activeReels, String gameId);

    public Message executeSpin(SpinParams spinParams);

    public Message endGame(String token, String gameId);

}
