package com.efun.service;

import com.efun.message.*;

import java.util.Map;

public interface MessageProviderService {

    public Message startGame(InitParams initParams, String gameId);

    public Message executeSpin(SpinParams spinParams);

    public Message endGame(EndParams endParams);

    public Map<String, Message> getSessions();

    public boolean isPossibleToStartGameBecauseOfChangingConfig();
    public void setTrigger(boolean trigger);


}
