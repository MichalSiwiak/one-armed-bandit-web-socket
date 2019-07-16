package com.efun.web;

import com.efun.config.GameConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AvailableInitConfigController {

    @Autowired
    GameConfig gameConfig;

    @RequestMapping(value = "/winLines", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public ResponseEntity<List<AvailableInitConfig>> getWinLines() {

        List<AvailableInitConfig> winLines = new ArrayList<>();
        for (int i = 0; i < gameConfig.getWinnings().size(); i++) {
            winLines.add(new AvailableInitConfig(i, "Win line " + (i + 1) + " with value " + gameConfig.getWinnings().get(i)));
        }
        return new ResponseEntity<>(winLines, HttpStatus.OK);
    }

    @RequestMapping(value = "/reels", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public ResponseEntity<List<AvailableInitConfig>> getReels() {

        List<AvailableInitConfig> reels = new ArrayList<>();
        for (int i = 0; i < gameConfig.getReels().size(); i++) {
            reels.add(new AvailableInitConfig(i, "Reel " + (i + 1) + " with size " + gameConfig.getReels().get(i).size()));
        }
        return new ResponseEntity<>(reels, HttpStatus.OK);
    }

    private class AvailableInitConfig {

        private int index;
        private String name;

        public AvailableInitConfig(int index, String name) {
            this.index = index;
            this.name = name;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}