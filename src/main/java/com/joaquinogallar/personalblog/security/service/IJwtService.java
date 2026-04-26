package com.joaquinogallar.personalblog.security.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface IJwtService {
    String generateToken(UserDetails user);
    String extractUsername(String token);
    boolean isTokenValid(String token, UserDetails userDetails);
}
