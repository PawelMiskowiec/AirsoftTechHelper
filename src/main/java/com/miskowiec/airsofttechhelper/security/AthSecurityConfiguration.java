package com.miskowiec.airsofttechhelper.security;


import com.miskowiec.airsofttechhelper.user.db.UserEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@EnableConfigurationProperties(AdminConfig.class)
@AllArgsConstructor
class AthSecurityConfiguration {

    private final AuthenticationConfiguration configuration;
    private final UserEntityRepository userEntityRepository;
    private final AdminConfig adminConfig;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authz) -> authz
                    .mvcMatchers(HttpMethod.GET, "/parts/**").permitAll()
                    .mvcMatchers(HttpMethod.POST, "/login", "/users").permitAll()
                    .anyRequest().authenticated()
            )
                .httpBasic()
            .and()
                .addFilterAfter(jsonAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http
            .csrf().disable();

        return http.build();
    }

    private JsonUsernameAuthenticationFilter jsonAuthenticationFilter() throws Exception {
        JsonUsernameAuthenticationFilter filter = new JsonUsernameAuthenticationFilter();
        filter.setAuthenticationManager(configuration.getAuthenticationManager());
        return filter;
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        AthUserDetailsService detailsService = new AthUserDetailsService(userEntityRepository, adminConfig);
        provider.setUserDetailsService(detailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
