package com.efun;

import com.efun.config.GameConfig;
import org.junit.Test;
import org.springframework.util.ResourceUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.*;

import static org.junit.Assert.assertNotNull;

public class YamlTest {

    @Test
    public void YamlTest() throws IOException {

        File file = ResourceUtils.getFile("classpath:game-configuration.yml");
        System.out.println(file.getName());
        System.out.println(file.getAbsolutePath());
        System.out.println(file.length());

        Yaml yaml = new Yaml();
        InputStream inputStream = new FileInputStream(file);
        GameConfig gameConfig = yaml.loadAs(inputStream, GameConfig.class);
        inputStream.close();
        assertNotNull(gameConfig);
    }

}
