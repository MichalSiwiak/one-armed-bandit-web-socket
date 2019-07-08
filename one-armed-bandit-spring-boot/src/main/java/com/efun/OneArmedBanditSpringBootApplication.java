package com.efun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


//@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class})
@SpringBootApplication
public class OneArmedBanditSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(OneArmedBanditSpringBootApplication.class, args);
    }

}