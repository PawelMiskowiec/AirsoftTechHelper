package com.example.airsofttechhelper.replica.web;

import com.example.airsofttechhelper.security.UserEntityDetails;
import com.example.airsofttechhelper.user.domain.UserEntity;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.List;

@TestConfiguration
public class SpringSecurityTesUserConfigForControllersTests {

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        UserDetails userDetails =
                testUserDetails();
        return new InMemoryUserDetailsManager(List.of(userDetails));
    }
    @Bean
    public UserDetails testUserDetails() {
        return new UserEntityDetails(
                new UserEntity(
                        "example@tech.com",
                        "pass123"
                )
        );
    }
}
