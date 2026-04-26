package com.joaquinogallar.personalblog.security.service;

import com.joaquinogallar.personalblog.security.dto.AuthResponse;
import com.joaquinogallar.personalblog.security.dto.RefreshRequest;
import com.joaquinogallar.personalblog.security.entity.CustomUserDetails;
import com.joaquinogallar.personalblog.security.entity.RefreshToken;
import com.joaquinogallar.personalblog.user.dto.UserRequest;
import com.joaquinogallar.personalblog.user.dto.UserResponse;
import com.joaquinogallar.personalblog.user.entity.Role;
import com.joaquinogallar.personalblog.user.entity.User;
import com.joaquinogallar.personalblog.user.mapper.UserMapper;
import com.joaquinogallar.personalblog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthResponse login(UserDetails userDetails) {
        User user = userRepository.findUserByUsername(userDetails.getUsername()) // todo: somehow users can log in with email, that's the correct behavior but it's doesn't make sense since the method it's `findUserByUsername`
                        .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        AuthResponse res = new AuthResponse(token, refreshToken.getToken());

        return res;
    }

    public UserResponse register(UserRequest request) {

        if (userRepository.existsByUsername(request.username())) {
            throw new RuntimeException("Username already exists");
        }

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .isActive(true)
                .roles(Set.of(Role.USER))
                .build();

        userRepository.save(user);

        return userMapper.mapUserToDto(user);
    }

    public UserResponse me(User user) {
        return userMapper.mapUserToDto(user);
    }

    public AuthResponse refresh(RefreshRequest refreshRequest) {
        RefreshToken refreshToken = refreshTokenService.validateRefreshToken(refreshRequest.getRefreshToken());

        User user = refreshToken.getUser();

        String newAccessToken = jwtService.generateToken(new CustomUserDetails(user));

        refreshTokenService.revokeRefreshToken(refreshToken.getToken());
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken.getToken())
                .build();

    }

    public void logout(RefreshRequest request) {
        refreshTokenService.revokeRefreshToken(request.getRefreshToken());
    }

}
