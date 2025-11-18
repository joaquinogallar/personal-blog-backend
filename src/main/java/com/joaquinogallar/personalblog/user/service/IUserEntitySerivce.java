package com.joaquinogallar.personalblog.user.service;

import com.joaquinogallar.personalblog.user.dto.UserEntityDto;
import com.joaquinogallar.personalblog.user.entity.UserEntity;

import java.util.List;
import java.util.UUID;

public interface IUserEntitySerivce {
    List<UserEntityDto> getAllUsers();
    UserEntityDto getUserById(UUID id);
    String createUser(UserEntityDto userEntity);
    String updateUser(UUID id, UserEntityDto userEntity);
    String deleteUser(UUID id);
}
