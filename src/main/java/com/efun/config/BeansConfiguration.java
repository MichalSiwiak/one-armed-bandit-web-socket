package com.efun.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;

@Configuration
public class BeansConfiguration {

    @Bean
    public GameConfig gameConfig() throws IOException {
        return parseGameConfigTest(ResourceUtils.getFile("classpath:config.json"));
    }

    public GameConfig parseGameConfigTest(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, GameConfig.class);
    }

}
