package com.example.airsofttechhelper;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Arrays;

@SpringBootApplication
public class AirsoftTechHelperApplication {

    public static void main(String[] args) {
        SpringApplication.run(AirsoftTechHelperApplication.class, args);
    }

}
