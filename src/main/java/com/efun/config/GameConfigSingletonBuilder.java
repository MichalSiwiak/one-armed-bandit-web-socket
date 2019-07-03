package com.efun.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.util.ResourceUtils;
import java.io.File;

//Singleton class returns instance of GameConfig class
public class GameConfigSingletonBuilder {

    private static GameConfig single_instance = null;

    private GameConfigSingletonBuilder() {
    }

    public static GameConfig getInstance() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            File file = ResourceUtils.getFile("classpath:config.yaml");
            single_instance = mapper.readValue(file, GameConfig.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return single_instance;
    }

}
