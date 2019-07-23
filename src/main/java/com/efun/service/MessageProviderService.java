package com.efun.service;

import com.efun.message.*;

public interface MessageProviderService {

    public Message startGame(InitParams initParams, String gameId);

    public Message executeSpin(SpinParams spinParams);

    public Message endGame(EndParams endParams);

}
