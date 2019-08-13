package com.efun.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class BeansConfiguration {

    @Bean
    public GameConfig gameConfig() throws IOException {
        return parseGameConfigFromClasspath("classpath:game-configuration.yml");
    }

    public GameConfig parseGameConfigFromClasspath(String fileName) throws IOException {
        Yaml yaml = new Yaml();
        InputStream inputStream = new FileInputStream(ResourceUtils.getFile(fileName));
        GameConfig gameConfig = yaml.loadAs(inputStream, GameConfig.class);
        inputStream.close();
        return gameConfig;
    }

    public GameConfig parseGameConfigFromFile(String fileName) throws IOException {
        Yaml yaml = new Yaml();
        InputStream inputStream = new FileInputStream(new File(fileName));
        GameConfig gameConfig = yaml.loadAs(inputStream, GameConfig.class);
        inputStream.close();
        return gameConfig;
    }


    //if jar packaging
    /*@Value("${path_to_config_file}")
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
    }*/

}
