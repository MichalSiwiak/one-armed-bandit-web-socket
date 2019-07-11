/*
package com.efun.tests;

import com.efun.message.MessageGameStart;
import com.efun.service.MessageProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
//Temporary class for tests - will moved to tests
public class TestsComponent {

    @Autowired
    MessageProviderService messageProviderService;

    @PostConstruct
    public void runAtStart() {

        //we have 5 reels from: 0,1,2,3,4
        int activeReelsArray[] = {2, 3, 4};
        List<Integer> winLines = Arrays.asList(3, 4, 5, 6, 7);
        List<Integer> activeReels = Arrays.stream(activeReelsArray).boxed().collect(Collectors.toList());
        MessageGameStart messageGameStart = messageProviderService.startGame(winLines, activeReels);
        String authorizationToken = messageGameStart.getAuthorizationToken();
        messageProviderService.executeSpin(5, authorizationToken);
        messageProviderService.executeSpin(5, authorizationToken);
        messageProviderService.executeSpin(5, authorizationToken);
        messageProviderService.executeSpin(5, authorizationToken);
        messageProviderService.executeSpin(5, authorizationToken);
        messageProviderService.endGame(authorizationToken);

    }
}
*/
