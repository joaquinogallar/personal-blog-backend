package com.joaquinogallar.personalblog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests((req) -> req
            .requestMatchers(HttpMethod.GET,
                        "/api/v1/posts",
                        "/api/v1/posts/**",
                        "/api/v1/comments",
                        "/api/v1/comments/**",
                        "/api/v1/tags",
                        "/api/v1/tags/**").permitAll()
            .requestMatchers(HttpMethod.POST,
                "/api/v1/comments"
            ).permitAll()
            .anyRequest().authenticated()
        )
        .logout((logout) -> logout.permitAll())
        .build();
    }

}
