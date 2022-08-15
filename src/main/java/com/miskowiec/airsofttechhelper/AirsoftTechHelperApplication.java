package com.miskowiec.airsofttechhelper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class AirsoftTechHelperApplication {

    public static void main(String[] args) {
        SpringApplication.run(AirsoftTechHelperApplication.class, args);
    }

}
