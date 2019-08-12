package com.efun.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.*;

@Configuration
public class BeansConfiguration {

    @Bean
    public GameConfig gameConfig() throws IOException {
        return parseGameConfig(ResourceUtils.getFile("classpath:game-configuration.yml"));
    }

    public GameConfig parseGameConfig(File file) throws IOException {
        Yaml yaml = new Yaml();
        InputStream inputStream = new FileInputStream(file);
        GameConfig gameConfig = yaml.loadAs(inputStream, GameConfig.class);
        inputStream.close();
        return gameConfig;
    }

}
