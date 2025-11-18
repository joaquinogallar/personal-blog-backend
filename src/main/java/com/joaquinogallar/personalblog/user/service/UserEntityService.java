package com.joaquinogallar.personalblog.user.service;

import com.joaquinogallar.personalblog.user.dto.ReqUserEntityDto;
import com.joaquinogallar.personalblog.user.dto.UserEntityDto;
import com.joaquinogallar.personalblog.user.entity.UserEntity;
import com.joaquinogallar.personalblog.user.mapper.UserEntityMapper;
import com.joaquinogallar.personalblog.user.repository.UserEntityRepository;
import jakarta.persistence.EntityNotFoundException;
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
        return userEntityMapper.mapUserToDto(userEntityRepository.findAll());
    }

    @Override
    public UserEntityDto getUserById(UUID id) {
        return userEntityMapper.mapUsertoDto(userEntityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User " + id + " not found")));
    }

    // ------------------------------------------------------------------------------------------------------------------------
    // CREATE
    @Override
    public String createUser(ReqUserEntityDto userEntity) {
        UserEntity newUser = UserEntity.builder()
                .username(userEntity.username())
                .email(userEntity.email())
                .passwordHash(userEntity.password()) // this should be hashed when security is implemented
                .build();

        userEntityRepository.save(newUser);

        return "User created successfully";
    }

    // ------------------------------------------------------------------------------------------------------------------------
    // UPDATE
    @Override
    public String updateUser(UUID id, UserEntityDto userEntity) {
        UserEntity user = userEntityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User " + id + " not found"));

        user.setUsername(userEntity.username());
        user.setEmail(userEntity.email());

        return "User " + id + " updated successfully";
    }

    // ------------------------------------------------------------------------------------------------------------------------
    // DELETE
    @Override
    public String deleteUser(UUID id) {
        return "User " + id + " deleted successfully";
    }
}
