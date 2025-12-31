package com.joaquinogallar.personalblog.security.controller;

import com.joaquinogallar.personalblog.user.dto.LoginRequest;
import com.joaquinogallar.personalblog.user.dto.UserResponse;
import com.joaquinogallar.personalblog.user.entity.User;
import com.joaquinogallar.personalblog.user.mapper.UserMapper;
import com.joaquinogallar.personalblog.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, UserMapper userMapper) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @PostMapping()
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.usernameOrEmail(),
                    loginRequest.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findUserByUsername(loginRequest.usernameOrEmail())
                .or(() -> userRepository.findUserByEmail(loginRequest.usernameOrEmail()))
                .orElseThrow(() -> new UsernameNotFoundException("Username or email doesn't exist"));


        return ResponseEntity.ok(userMapper.mapUserToDto(user));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(Authentication authentication) {
        User user = userRepository.findUserByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Error"));

        return ResponseEntity.ok(userMapper.mapUserToDto(user));
    }

}
