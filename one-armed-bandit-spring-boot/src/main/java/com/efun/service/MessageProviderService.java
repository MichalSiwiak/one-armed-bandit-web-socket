package com.efun.service;

import com.efun.message.MessageGameEnd;
import com.efun.message.MessageGameSpin;
import com.efun.message.MessageGameStart;

import java.util.List;

public interface MessageProviderService {

    public MessageGameStart startGame(List<Integer> winLines, List<Integer> activeReels, String gameId);

    public MessageGameSpin executeSpin(int rno, int bet, String token);

    public MessageGameEnd endGame(String token);

}
