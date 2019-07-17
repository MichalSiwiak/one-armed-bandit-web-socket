package com.efun.service;

import com.efun.message.Message;
import com.efun.message.MessageGameEnd;
import com.efun.message.MessageGameSpin;
import com.efun.message.MessageGameStart;

import java.util.List;

public interface MessageProviderService {

    public Message startGame(List<Integer> winLines, List<Integer> activeReels, String gameId);

    public Message executeSpin(int rno, int bet, String token);

    public Message endGame(String token);

}
