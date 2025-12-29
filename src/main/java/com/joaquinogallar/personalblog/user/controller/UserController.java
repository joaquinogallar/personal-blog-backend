package com.joaquinogallar.personalblog.user.controller;

import com.joaquinogallar.personalblog.user.dto.UserRequest;
import com.joaquinogallar.personalblog.user.dto.UserResponse;
import com.joaquinogallar.personalblog.user.service.IUserSerivce;
import com.joaquinogallar.personalblog.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(name = "/api/v1/users")
public class UserController {

    private final IUserSerivce userEntityService;

    public UserController(UserService userEntityService) {
        this.userEntityService = userEntityService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userEntityService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID userId) {
        return ResponseEntity.ok(userEntityService.getUserById(userId));
    }

    @GetMapping("/{userEmail}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String userEmail) {
        return ResponseEntity.ok(userEntityService.getUserByEmail(userEmail));
    }

    @GetMapping("/{userUsername}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String userUsername) {
        return ResponseEntity.ok(userEntityService.getUserByUsername(userUsername));
    }

    @PostMapping
    public ResponseEntity<String> createUser(UserRequest user) {
        return ResponseEntity.ok(userEntityService.createUser(user));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable UUID userId, @RequestBody UserRequest user) {
        return ResponseEntity.ok(userEntityService.updateUser(userId, user));
    }

    @DeleteMapping("/{userId}")
     public ResponseEntity<String> deleteUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(userEntityService.deleteUser(userId));
     }
}
