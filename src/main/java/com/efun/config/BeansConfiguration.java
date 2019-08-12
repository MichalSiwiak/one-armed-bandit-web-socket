package com.efun.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.*;

@Configuration
public class BeansConfiguration {

    @Value("${path_to_config_file}")
    private String pathToConfigFile;

    @Bean
    public GameConfig gameConfig() throws IOException {
        return parseGameConfig(pathToConfigFile);
    }

    public GameConfig parseGameConfig(String fileName) throws IOException {
        Yaml yaml = new Yaml();
        InputStream inputStream = new FileInputStream(new File(fileName));
        GameConfig gameConfig = yaml.loadAs(inputStream, GameConfig.class);
        inputStream.close();
        return gameConfig;
    }

}
