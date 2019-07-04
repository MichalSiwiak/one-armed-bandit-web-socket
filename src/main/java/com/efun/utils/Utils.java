package com.efun.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.util.ResourceUtils;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static Map<String,String> readDatabaseSettings(){
        Map<String,String> connectionData = null;
        try {
            File file = ResourceUtils.getFile("classpath:db_connection.yaml");
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            connectionData = mapper.readValue(file, HashMap.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connectionData;
    }
}
