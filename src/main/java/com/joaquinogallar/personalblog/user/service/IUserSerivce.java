package com.joaquinogallar.personalblog.user.service;

import com.joaquinogallar.personalblog.user.dto.UserRequest;
import com.joaquinogallar.personalblog.user.dto.UserResponse;

import java.util.List;
import java.util.UUID;

public interface IUserSerivce {
    List<UserResponse> getAllUsers();
    UserResponse getUserById(UUID id);
    String createUser(UserRequest userEntity);
    String updateUser(UUID id, UserResponse userEntity);
    String deleteUser(UUID id);
}
