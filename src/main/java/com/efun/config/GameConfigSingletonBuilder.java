package com.efun.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.util.ResourceUtils;

import java.io.File;

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
