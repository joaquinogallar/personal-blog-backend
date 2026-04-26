package com.joaquinogallar.personalblog.security.service;

import com.joaquinogallar.personalblog.security.dto.AuthResponse;
import com.joaquinogallar.personalblog.security.dto.RefreshRequest;
import com.joaquinogallar.personalblog.user.dto.UserRequest;
import com.joaquinogallar.personalblog.user.dto.UserResponse;
import com.joaquinogallar.personalblog.user.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface IAuthService {
    AuthResponse login(UserDetails userDetails);
    UserResponse register(UserRequest request);
    UserResponse me(User user);
    AuthResponse refresh(RefreshRequest refreshRequest);
    void logout(RefreshRequest request);
}
