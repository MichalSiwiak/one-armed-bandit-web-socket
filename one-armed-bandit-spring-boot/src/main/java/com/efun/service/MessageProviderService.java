package com.efun.service;

import com.efun.message.MessageGameEnd;
import com.efun.message.MessageGameSpin;
import com.efun.message.MessageGameStart;

import java.util.List;

public interface MessageProviderService {

    public MessageGameStart startGame(List<Integer> winLines, List<Integer> activeReels);

    public MessageGameSpin executeSpin(int bet, String token);

    public MessageGameEnd endGame(String token);

}
