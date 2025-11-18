package com.joaquinogallar.personalblog.user.service;

import com.joaquinogallar.personalblog.user.dto.UserEntityDto;
import com.joaquinogallar.personalblog.user.mapper.UserEntityMapper;
import com.joaquinogallar.personalblog.user.repository.UserEntityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserEntityService implements IUserEntitySerivce {

    private final UserEntityRepository userEntityRepository;
    private final UserEntityMapper userEntityMapper;

    public UserEntityService(UserEntityRepository userEntityRepository, UserEntityMapper userEntityMapper) {
        this.userEntityRepository = userEntityRepository;
        this.userEntityMapper = userEntityMapper;
    }

    // GET
    @Override
    public List<UserEntityDto> getAllUsers() {
        return List.of();
    }

    @Override
    public UserEntityDto getUserById(UUID id) {
        return null;
    }

    // ------------------------------------------------------------------------------------------------------------------------
    // CREATE
    @Override
    public String createUser(UserEntityDto userEntity) {
        return "User created successfully";
    }

    // ------------------------------------------------------------------------------------------------------------------------
    // UPDATE
    @Override
    public String updateUser(UUID id, UserEntityDto userEntity) {
        return "User " + id + " updated successfully";
    }

    // ------------------------------------------------------------------------------------------------------------------------
    // DELETE
    @Override
    public String deleteUser(UUID id) {
        return "User " + id + " deleted successfully";
    }
}
