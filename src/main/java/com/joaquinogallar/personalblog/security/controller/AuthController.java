package com.joaquinogallar.personalblog.security.controller;

import com.joaquinogallar.personalblog.security.dto.AuthResponse;
import com.joaquinogallar.personalblog.security.dto.RefreshRequest;
import com.joaquinogallar.personalblog.security.service.AuthService;
import com.joaquinogallar.personalblog.security.service.JwtService;
import com.joaquinogallar.personalblog.user.dto.LoginRequest;
import com.joaquinogallar.personalblog.user.dto.UserRequest;
import com.joaquinogallar.personalblog.user.dto.UserResponse;
import com.joaquinogallar.personalblog.user.entity.Role;
import com.joaquinogallar.personalblog.user.entity.User;
import com.joaquinogallar.personalblog.user.mapper.UserMapper;
import com.joaquinogallar.personalblog.user.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager, AuthService authService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @PostMapping()
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.usernameOrEmail(),
                        loginRequest.password()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(authService.login(userDetails));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid UserRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(Authentication authentication) {
        User user = userRepository.findUserByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Error"));

        return ResponseEntity.ok(authService.me(user));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshRequest refreshRequest) {
        return ResponseEntity.ok(authService.refresh(refreshRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody RefreshRequest request) {
        authService.logout(request);
        return ResponseEntity.noContent().build();
    }
}
