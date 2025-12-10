package com.joaquinogallar.personalblog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.joaquinogallar.personalblog.user.entity.Role;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        UserDetails user =
        User.builder()
            .username("test")
            .password(encoder.encode("123456"))
            .roles(Role.ROLE_ADMIN.toString(), Role.ROLE_USER.toString())
            .build();
        return new InMemoryUserDetailsManager(user);
    }
}
