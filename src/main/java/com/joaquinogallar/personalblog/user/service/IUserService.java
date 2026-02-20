package com.joaquinogallar.personalblog.user.service;

import com.joaquinogallar.personalblog.user.dto.UserRequest;
import com.joaquinogallar.personalblog.user.dto.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    Page<UserResponse> getAllUsers(Pageable pageable);
    UserResponse getUserById(UUID id);
    UserResponse getUserByEmail(String email);
    UserResponse getUserByUsername(String username);
    String createUser(UserRequest userEntity);
    String updateUser(UUID id, UserRequest userEntity);
    String deleteUser(UUID id);
}
