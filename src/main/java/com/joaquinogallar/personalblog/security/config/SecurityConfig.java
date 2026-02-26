package com.joaquinogallar.personalblog.security.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.joaquinogallar.personalblog.user.entity.Role;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests((req) -> req
                        // public - no auth required
                        .requestMatchers(HttpMethod.GET,
                                "/api/v1/posts/**",
                                "/api/v1/comments/**",
                                "/api/v1/tags",
                                "/api/v1/tags/**").permitAll()

                        // auth endpoints
                        .requestMatchers("/api/v1/auth/**").permitAll()

                        // anon comments
                        .requestMatchers(HttpMethod.POST, "/api/v1/comments").permitAll()

                        // admin only
                        .requestMatchers(HttpMethod.GET, "/api/v1/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/posts", "/api/v1/posts/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/posts", "/api/v1/posts/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/posts", "/api/v1/posts/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/tags", "/api/v1/tags/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/tags", "/api/v1/tags/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/tags", "/api/v1/tags/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/posts", "/api/v1/posts/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/tags", "/api/v1/tags/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(
                                (request, response, authException) ->
                                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
                        )
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
