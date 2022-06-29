package com.example.airsofttechhelper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AirsoftTechHelperApplication {

    public static void main(String[] args) {
        SpringApplication.run(AirsoftTechHelperApplication.class, args);
    }

}
