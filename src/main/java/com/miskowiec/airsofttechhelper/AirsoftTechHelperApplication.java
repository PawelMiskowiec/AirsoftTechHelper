package com.miskowiec.airsofttechhelper;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
@EnableConfigurationProperties
public class AirsoftTechHelperApplication {

    public static void main(String[] args) {
        SpringApplication.run(AirsoftTechHelperApplication.class, args);

    }
}
